# AGENTS.md

This file provides guidance to AI coding agents (Claude Code, etc.) when working with code in this repository.

## Project overview

A Korean-language web-based groupware system (그룹웨어) built on Spring Boot 2.7 (Spring Framework 5.3) + MyBatis 3 +
MariaDB, packaged as a `war` with Gradle. It's a sample/practice project built on top of the
[Project9](https://github.com/gujc71/project9/) web template. Implemented features: monthly calendar/schedule,
e-approval (기안/결재 documents), internal mail (compose/inbox/sent, IMAP import), and a bulletin board system with
configurable board groups.

- Java 1.8 source/target compatibility (`build.gradle`), running on a JDK 17 toolchain.
- No automated test suite exists (`src/test` is not present); verification is manual, through the running app.
- No linter/formatter is configured (the project no longer builds via Eclipse; there is no Eclipse project/
  Checkstyle metadata checked in).
- **Migration history / current stage**: this was originally a plain Spring 4 + `web.xml` app. It went through a
  staged migration: (1) modernize dependencies within the Spring 4.x/`javax.*` generation, (2) **convert to Spring
  Boot 2.7** (current stage — still `javax.*`, since Boot 2.x is the last major line built on Spring Framework 5.x
  rather than 6). A later stage could still jump to Spring Boot 3 / Spring 6, which is when the `javax` → `jakarta`
  namespace switch would actually happen (Spring's own MVC classes only support `jakarta.servlet` from Spring 6
  onward, so that switch cannot happen independently of a Spring 6 upgrade). `org.apache.poi:poi`/`poi-ooxml` are
  intentionally still pinned at 3.14: `gu.common.MakeExcel` drives Excel export through
  `net.sf.jxls:jxls-core:1.0.6`, an unmaintained (2011-era) library that calls POI 3.x internals directly, so
  bumping POI without first replacing jxls risks breaking Excel export (not yet re-verified after the Boot move —
  see Known gaps below).

## Build and run

Spring Boot Gradle project (Gradle Wrapper checked in) — no IDE project files are checked in, so any editor/IDE can
be pointed at it, but building/running goes through Gradle rather than an IDE-managed server. `gu.GroupwareApplication`
is the entry point (`@SpringBootApplication` + `SpringBootServletInitializer`); it replaces the old `web.xml` and
`@ImportResource`s the legacy Spring bean XML unchanged (see Architecture below).

```
./gradlew bootRun        # run with embedded Tomcat at http://localhost:8080/groupware9/ (gradlew.bat on Windows)
./gradlew bootWar         # build the executable WAR at build/libs/project9-1.0.war
./gradlew war             # build a plain (non-executable) WAR at build/libs/project9-1.0-plain.war, for deploying
                           # to an external servlet container instead
```

`bootWar`'s output is runnable both ways: `java -jar build/libs/project9-1.0.war` starts it standalone (embedded
Tomcat), and the same file can also be dropped into an external Tomcat's `webapps/`. Default seeded logins:
`admin/admin`, `user1/user1`, `user2/user2`, etc. `server.servlet.context-path` is pinned to `/groupware9` in
`application.yml` so the URL matches regardless of how it's run.

### Database setup

MariaDB database named `groupware9` is required (JDBC driver is `org.mariadb.jdbc` — MariaDB Connector/J — not the
MySQL driver, even though the codebase/docs still say "MariaDB/MySQL" since the schema is wire-compatible with
either). Connection settings (driver class, URL, username, password) live in
`src/main/resources/config/application.yml` under a custom `db.*` key (not Boot's own `spring.datasource.*`) —
`src/main/resources/spring/applicationContext.xml`'s `dataSource` bean reads them via `${db.*}` placeholders
resolved by a `YamlPropertiesFactoryBean` + `context:property-placeholder`, predating the Boot migration and left
as-is deliberately (Boot's own `DataSourceAutoConfiguration` never activates because no `spring.datasource.*` keys
are set, so there's no conflict with this manual wiring). Defaults are `root`/`gujc1004` against
`jdbc:log4jdbc:mariadb://localhost/groupware9` (wrapped by `log4jdbc` for SQL logging; the real driver behind it is
`net.sf.log4jdbc.sql.jdbcapi.DriverSpy`, set via `db.driverClassName`).

**Docker (recommended):** `docker compose up -d` starts a MariaDB container (`docker-compose.yml`) already matching
those credentials on `localhost:3306`, and auto-loads `tables.sql`/`tableData.sql` on first start via
`docker/mariadb-init/01-init.sh` — nothing else to configure. That script bypasses docker-entrypoint-initdb.d's
built-in `*.sql` handling (which runs `mysql --binary-mode` and breaks the `DELIMITER`-based routines in
`tables.sql`) by invoking a plain `mysql` client instead. The container also sets `--lower-case-table-names=1`
because `tables.sql` creates tables in UPPERCASE while `tableData.sql` seeds them by lowercase name — fine on
case-insensitive Windows/MySQL, but Linux MariaDB defaults to case-sensitive table names. Re-running
`docker compose up -d` reuses the existing data volume (`groupware_groupware9-mariadb-data`); `docker compose down
-v` wipes it to reseed from scratch.

**Manual (non-Docker) setup:**
1. Create the `groupware9` database.
2. Run `tables.sql` then `tableData.sql` at the repo root to create schema and seed data.
3. Adjust `src/main/resources/config/application.yml`'s `db.*` values if your local credentials differ from
   `root`/`gujc1004`.

`groupware9.erm` / `project9.erd` are ERD design files (openable in ERD tooling) documenting the schema design.

### Logging

Logback config: `src/main/resources/logback.xml`; log4jdbc config: `src/main/resources/log4jdbc.log4j2.properties`.
Runtime logs land in `logs/project9.log`.

## Architecture

### Layering: Controller → Service → MyBatis mapper XML

Every feature module follows the same 3(+1)-file pattern inside a `gu.<module>` package (e.g. `gu.board`,
`gu.mail`, `gu.schedule`, `gu.sign`, `gu.member`):

- `*Ctr.java` — `@Controller`, one method per `@RequestMapping` endpoint, returns a JSP view name (resolved under
  `/WEB-INF/jsp/`) or writes JSON directly for Ajax endpoints (see `UtilEtc.responseJsonValue`). Admin-only
  controllers live under `gu.admin.<module>` (board groups, org/department, admin sign-doc types, code tables).
- `*Svc.java` — `@Service`, injects `SqlSessionTemplate sqlSession` (MyBatis) directly — there is no separate DAO/
  Repository layer. Multi-statement writes wrap manual transactions using an injected
  `DataSourceTransactionManager` (`DefaultTransactionDefinition` + explicit `commit`/`rollback` in a try/catch),
  rather than `@Transactional` (nothing in the codebase uses `@Transactional`; if it ever does, Spring Boot's own
  `TransactionAutoConfiguration` picks up the `txManager` bean automatically — the old XML `tx:advice`/
  `tx:annotation-driven` declarations were removed during the Boot migration because they registered
  infrastructure beans under names Boot's auto-configuration also uses, which fails startup).
- `*VO.java` — plain data-holder classes bound from request params (Spring auto-binds request parameters to VO
  fields) and used as MyBatis parameter/result types.
- `src/main/resources/sql/<module>.xml` — MyBatis mapper XML with SQL for that module. **Mapper `<select>/<insert>/
  <update>/<delete>` `id`s are referenced from Java code by their bare id (e.g.
  `sqlSession.selectOne("selectBoardGroupOne4Used", ...)`), not namespace-qualified**, even though every mapper
  declares a `namespace`. Keep mapper statement ids unique across the whole `sql/` directory, not just within one
  file, when adding new ones.

Cross-module reuse: controllers freely `@Autowired` services from other modules (e.g. `BoardCtr` uses both
`BoardSvc` and `BoardGroupSvc`, plus the shared `EtcSvc`). `gu.etc.EtcSvc.setCommonAttribute(userno, modelMap)` is
the standard call at the top of most authenticated controller methods to populate common sidebar/nav model
attributes.

### Request flow / security

- No `web.xml` — `gu.GroupwareApplication` (`@SpringBootApplication`) is the entry point, and
  `@ImportResource({"classpath:spring/applicationContext.xml", "classpath:spring/dispatcher-servlet.xml"})` loads
  the legacy bean XML (now under `src/main/resources/spring/`, moved off the servlet-relative `WEB-INF/` path so
  it's a normal classpath resource) essentially unchanged: `dataSource`/MyBatis/`messageSource`/
  `InternalResourceViewResolver`/`CommonsMultipartResolver` beans are still XML. Boot uses a single unified
  `ApplicationContext` (no separate root + DispatcherServlet child context), so both files load into the same
  context.
- Interceptors and static resource mappings moved out of `dispatcher-servlet.xml` into `gu.config.WebMvcConfig`
  (a `WebMvcConfigurer` `@Bean`) — the `<mvc:*>` XML namespace elements (`annotation-driven`, `interceptors`,
  `resources`) all register shared MVC infrastructure beans under names Spring Boot's `WebMvcAutoConfiguration`
  independently defines too (e.g. `mvcUrlPathHelper`), which fails startup with a bean-definition-override error —
  Java config avoids that collision. `WebMvcConfig` registers the same two interceptors as before:
  - `gu.common.LoginInterceptor` on `/**` (excluding `/memberLogin`, `/memberLoginChk`, `/js/**`, `/css/**`,
    `/images/**`) — redirects to `memberLogin` if `session.getAttribute("userno")` is absent.
  - `gu.common.AdminInterceptor` on `/ad*` — additionally requires `session.getAttribute("userrole")` to equal
    `"A"`, else redirects to `noAuthMessage`.
- Error pages (404/500/generic `Exception` → JSPs under `WEB-INF/jsp/common/`), UTF-8 request/response encoding,
  and session timeout — previously `web.xml` `<error-page>`/filter/`<session-config>` entries — are now an
  `ErrorPageRegistrar` `@Bean` in `GroupwareApplication` plus `server.servlet.encoding.*`/
  `server.servlet.session.timeout` in `application.yml`.
- There is no Spring Security; auth state is plain `HttpSession` attributes (`userno`, `userid`, `userrole`,
  `usernm`) set at login. Per-record write/delete authorization (e.g. board edit/delete) is checked ad hoc in the
  service layer via `select*AuthChk` mapper queries returning null on failure — controllers then return the
  `common/noAuth` view or a JSON `"FailAuth"`/`"Fail"` payload for Ajax calls.
- i18n: `ReloadableResourceBundleMessageSource` over `classpath:/message/message[_en]`, session-scoped locale
  resolver defaulting to `ko`. Access messages through the `gu.common.LocaleMessage` bean.

### Common utility layer (`gu.common`)

Shared across modules — check here before adding a new helper:
- `FileUtil` / `FileVO` / `FileDownload` — file upload persistence and download streaming.
- `Upload4ckeditor` — CKEditor image upload endpoint (the JS editor lives under
  `src/main/webapp/js/ckeditor`).
- `TreeMaker` / `TreeVO` — builds hierarchical JSON (used for board-group and org trees via Ajax, see
  `boardListByAjax`).
- `SearchVO` / `PageVO` — base classes for list search/paging params; `pageCalculate(count)` computes
  `startRow`/`endRow`/`rowStart` used by MyBatis `LIMIT` clauses. Module-specific search VOs (e.g.
  `BoardSearchVO`) extend these.
- `Field3VO` — generic 3-field parameter carrier passed into MyBatis calls that need an ad hoc small tuple of
  params instead of a dedicated VO.
- `UtilEtc` — misc helpers including `responseJsonValue(response, value)` for simple Ajax/JSON responses.
- `MakeExcel` — Excel export (jxls/POI) using templates from `src/main/webapp/WEB-INF/templete/`.
- `Util4calen` — calendar/date-grid helpers for the schedule module.

### Frontend

Server-rendered JSP views under `src/main/webapp/WEB-INF/jsp/<module>/`, one subfolder per module mirroring the
Java package layout, plus `common/` for shared fragments (`navigation.jsp`, error pages, paging fragment) and
`main/` for the dashboard/index. Static assets under `src/main/webapp/{css,js,images}` — notable bundled
third-party libs: CKEditor (`js/ckeditor`), a tree widget (`js/dynatree`), and a date picker (`js/datepicker`),
themed with the `sb-admin` CSS template.

## Conventions to follow when extending a module

- Add a new endpoint in the module's `*Ctr.java`, business/query logic in `*Svc.java`, and the SQL in
  `src/main/resources/sql/<module>.xml` — don't put SQL in Java or business logic in JSP.
- Pull `userno` from `request.getSession().getAttribute("userno")` for the current-user id in write operations;
  don't add a new auth mechanism.
- When a write needs multi-statement atomicity, follow the existing manual-transaction pattern in the module's
  `Svc` class (see `BoardSvc.insertBoard`/`insertBoardLike`) rather than introducing `@Transactional` inconsistently
  with neighboring methods in the same file.
- New MyBatis statement ids must be unique repo-wide (see mapper note above).
