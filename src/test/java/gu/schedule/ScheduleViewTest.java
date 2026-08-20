package gu.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import gu.support.AuthenticatedMvcTestBase;

/**
 * JSP -> Thymeleaf 전환 대상: schedule/{SchList,SchForm,SchRead,SchRead4Ajax}.
 * board 모듈 전환 때와 동일하게 실제(Docker) 개발 DB를 그대로 사용하는 통합 테스트 -
 * 별도 테스트 DB/프로파일이 없다(AGENTS.md 참고). 테스트가 만든 일정은 각 테스트가 직접 정리한다.
 */
class ScheduleViewTest extends AuthenticatedMvcTestBase {

    @Autowired
    private SchSvc schSvc;

    private SchVO createdSchedule;

    @AfterEach
    void cleanup() {
        if (createdSchedule != null && createdSchedule.getSsno() != null) {
            schSvc.deleteSch(createdSchedule);
            createdSchedule = null;
        }
    }

    private SchVO insertTestSchedule(String title, String date) {
        SchVO vo = new SchVO();
        vo.setSstitle(title);
        vo.setSstype("1");
        vo.setSsstartdate(date);
        vo.setSsstarthour("09");
        vo.setSsstartminute("00");
        vo.setSsenddate(date);
        vo.setSsendhour("18");
        vo.setSsendminute("00");
        vo.setSsrepeattype("1");
        vo.setSsisopen("Y");
        vo.setSscontents("TDD 테스트용 일정 내용");
        vo.setUserno(loginUserno);
        schSvc.insertSch(vo);
        createdSchedule = vo;
        return vo;
    }

    @Test
    void schList_rendersMonthlyGridWithHeaderAndAddButton() throws Exception {
        mockMvc.perform(get("/schList").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("schedule/SchList"))
                .andExpect(content().string(containsString("월간 일정")))
                .andExpect(content().string(containsString("일정추가")))
                .andExpect(content().string(containsString("calendarColumnHead")));
    }

    @Test
    void schList_specificMonth_showsYearMonthAndNavLinksToAdjacentMonths() throws Exception {
        mockMvc.perform(get("/schList").param("year", "2026").param("month", "8").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("2026")))
                .andExpect(content().string(containsString("8")))
                .andExpect(content().string(containsString("month=7")))
                .andExpect(content().string(containsString("month=9")));
    }

    @Test
    void schList_showsOwnScheduleAsEditableLinkAndAppliesSundayStyling() throws Exception {
        insertTestSchedule("TDD 목록노출테스트", "2026-08-23"); // 2026-08-23 is a Sunday

        mockMvc.perform(get("/schList").param("year", "2026").param("month", "8").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("TDD 목록노출테스트")))
                .andExpect(content().string(containsString("schForm?ssno=" + createdSchedule.getSsno())))
                .andExpect(content().string(containsString("calendarColumnSunDay")));
    }

    @Test
    void schForm_newEvent_showsBlankTitleAndTypeOptions() throws Exception {
        mockMvc.perform(get("/schForm").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("schedule/SchForm"))
                .andExpect(content().string(containsString("일정명")))
                .andExpect(content().string(containsString("업무")))
                .andExpect(content().string(containsString("회의")));
    }

    @Test
    void schForm_editExisting_prefillsTitleAndSelectsMatchingType() throws Exception {
        insertTestSchedule("TDD 수정화면테스트", "2026-08-24");

        mockMvc.perform(get("/schForm").param("ssno", createdSchedule.getSsno()).session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("TDD 수정화면테스트")))
                .andExpect(content().string(containsString("value=\"09\" selected=\"selected\"")));
    }

    @Test
    void schRead_showsEventDetailAndTranslatedCodeNames() throws Exception {
        insertTestSchedule("TDD 읽기화면테스트", "2026-08-25");

        mockMvc.perform(get("/schRead").param("ssno", createdSchedule.getSsno()).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("schedule/SchRead"))
                .andExpect(content().string(containsString("TDD 읽기화면테스트")))
                .andExpect(content().string(containsString("업무")))
                .andExpect(content().string(containsString("공개")));
    }

    @Test
    void schRead4Ajax_nonRepeatingEvent_showsStartAndEndTimeRange() throws Exception {
        insertTestSchedule("TDD 툴팁테스트", "2026-08-26");

        mockMvc.perform(get("/schRead4Ajax")
                        .param("ssno", createdSchedule.getSsno())
                        .param("cddate", "2026-08-26")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("schedule/SchRead4Ajax"))
                .andExpect(content().string(containsString("TDD 툴팁테스트")))
                .andExpect(content().string(containsString("09:00")))
                .andExpect(content().string(containsString("18:00")));
    }

    @Test
    void schSaveThenSchDelete_createsThenSoftDeletesEvent() throws Exception {
        mockMvc.perform(post("/schSave")
                        .session(session)
                        .param("sstitle", "TDD 저장삭제테스트")
                        .param("sstype", "1")
                        .param("ssstartdate", "2026-08-27")
                        .param("ssstarthour", "09")
                        .param("ssstartminute", "00")
                        .param("ssenddate", "2026-08-27")
                        .param("ssendhour", "18")
                        .param("ssendminute", "00")
                        .param("ssrepeattype", "1")
                        .param("ssisopen", "Y")
                        .param("sscontents", "TDD 저장삭제테스트 내용"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/schList"));

        MonthVO month = new MonthVO();
        month.setYear("2026");
        month.setMonth("8");
        java.util.List<?> days = schSvc.selectCalendar(month, loginUserno);
        SchDetailVO savedDetail = days.stream()
                .map(d -> (CalendarVO) d)
                .filter(d -> "2026-08-27".equals(d.getCddate()))
                .flatMap(d -> d.getList().stream())
                .map(i -> (SchDetailVO) i)
                .filter(i -> "TDD 저장삭제테스트".equals(i.getSstitle()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("saved schedule not found in calendar"));

        SchVO toDelete = new SchVO();
        toDelete.setSsno(savedDetail.getSsno());
        try {
            mockMvc.perform(get("/schDelete").param("ssno", toDelete.getSsno()).session(session))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/schList"));

            SchVO afterDelete = schSvc.selectSchOne4Read(toDelete);
            assertThat(afterDelete).isNull();
        } finally {
            schSvc.deleteSch(toDelete); // idempotent safety net if the schDelete assertion above failed
        }
    }
}
