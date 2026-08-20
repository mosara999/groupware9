# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

A Korean-language web-based groupware system (그룹웨어) built on Spring 4 + MyBatis 3 + MariaDB/MySQL, packaged as a
`war` with Gradle. It's a sample/practice project built on top of the [Project9](https://github.com/gujc71/project9/)
web template. Implemented features: monthly calendar/schedule, e-approval (기안/결재 documents), internal mail
(compose/inbox/sent, IMAP import), and a bulletin board system with configurable board groups.

- Java 1.8 source/target compatibility (`build.gradle`). (Originally Maven declared source/target 1.7 while Eclipse
  actually compiled at 1.8; the project was migrated to Gradle and standardized on 1.8, which also matches the JDK
  17 toolchain used to run the Gradle build itself.)
- No automated test suite exists (`src/test` is not present); verification is manual, through the running app.
- No linter/formatter is configured (the project no longer builds via Eclipse; there is no Eclipse project/
  Checkstyle metadata checked in).
- Dependencies (`build.gradle`) are modernized to their latest release within the Spring 4.x/`javax.*` generation —
  this is step 1 of a staged Spring 4 → 6 migration; the `javax` → `jakarta` namespace switch and the Spring 5/6
  jump itself haven't happened yet. `org.apache.poi:poi`/`poi-ooxml` are the one exception, intentionally left at
  3.14: `gu.common.MakeExcel` drives Excel export through `net.sf.jxls:jxls-core:1.0.6`, an unmaintained (2011-era)
  library that calls POI 3.x internals directly, so bumping POI without first replacing jxls risks breaking Excel
  export in a way that can't be verified without a running Tomcat + browser to actually download a file.

## Build and run

Plain Gradle project (Gradle Wrapper checked in) — no IDE project files are checked in, so any editor/IDE can be
pointed at it, but building/running goes through Gradle rather than an IDE-managed server.

```
./gradlew compileJava   # compile (gradlew.bat on Windows cmd/PowerShell outside Git Bash)
./gradlew war            # build the WAR at build/libs/project9-1.0.war (src/test is absent, so no tests run)
```

To actually run the app: deploy the built WAR to a Tomcat instance — e.g. copy `build/libs/project9-1.0.war` into
Tomcat's `webapps/` as `groupware9.war` (Tomcat derives the context path from the deployed file/folder name) — then
browse to `http://localhost:8080/groupware9/`. Default seeded logins: `admin/admin`, `user1/user1`, `user2/user2`,
etc.

### Database setup

MariaDB database named `groupware9` is required (JDBC driver is `org.mariadb.jdbc` — MariaDB Connector/J — not the
MySQL driver, even though the codebase/docs still say "MariaDB/MySQL" since the schema is wire-compatible with
either). Connection settings (driver class, URL, username, password) live in
`src/main/resources/config/application.yml`, not in the XML — `applicationContext.xml`'s `dataSource` bean reads
them via `${db.*}` placeholders resolved by a `YamlPropertiesFactoryBean` + `context:property-placeholder` (needs
`org.yaml:snakeyaml` on the classpath, already in `build.gradle`). Defaults are `root`/`gujc1004` against
`jdbc:log4jdbc:mariadb://localhost/groupware9` (wrapped by `log4jdbc` for SQL logging; the real driver behind it is
`net.sf.log4jdbc.sql.jdbcapi.DriverSpy`, set via `db.driverClassName`). Classic Spring XML bean wiring itself (this
file, `dispatcher-servlet.xml`, `web.xml`) has no YAML equivalent in vanilla Spring — only the externalized values
are YAML.

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
3. Adjust `applicationContext.xml`'s `dataSource` bean if your local credentials differ from `root`/`gujc1004`.

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
  rather than `@Transactional`, even though `applicationContext.xml` also wires up `tx:advice`/annotation-driven
  transactions.
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

- `dispatcher-servlet.xml` component-scans the whole `gu` package, maps views through
  `InternalResourceViewResolver` (`/WEB-INF/jsp/<view>.jsp`), and registers two interceptors:
  - `gu.common.LoginInterceptor` on `/**` (excluding `/memberLogin`, `/memberLoginChk`, `/js/**`, `/css/**`,
    `/images/**`) — redirects to `memberLogin` if `session.getAttribute("userno")` is absent.
  - `gu.common.AdminInterceptor` on `/ad*` — additionally requires `session.getAttribute("userrole")` to equal
    `"A"`, else redirects to `noAuthMessage`.
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
