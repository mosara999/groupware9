-- COM_DATE 백필: makeCalendar() 프로시저가 2018-01-01~2020-12-31 까지만 생성해 두어
-- 월간 일정(schList) 그리드가 그 범위 밖의 달(예: 2026-08)에서는 날짜가 하나도 표시되지 않는 문제 해결용.
-- 2021-01-01 ~ 2035-12-31 구간을 채운다. 이미 존재하는 날짜는 건너뛴다.

SET SESSION max_recursive_iterations = 10000;

INSERT INTO COM_DATE (CDDATE, CDYEAR, CDMM, CDDD, CDWEEKOFYEAR, CDWEEKOFMONTH, CDWEEK, CDDAYOFWEEK)
WITH RECURSIVE seq AS (
    SELECT DATE('2021-01-01') AS d
    UNION ALL
    SELECT d + INTERVAL 1 DAY FROM seq WHERE d < '2035-12-31'
)
SELECT d, YEAR(d), MONTH(d), DAY(d), WEEKOFYEAR(d), FLOOR((DAYOFMONTH(d) - 1) / 7) + 1, WEEK(d), DAYOFWEEK(d)
FROM seq
WHERE NOT EXISTS (SELECT 1 FROM COM_DATE WHERE COM_DATE.CDDATE = d);
