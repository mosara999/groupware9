<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>개인정보 처리방침 - Groupware 9</title>

    <style>
        :root {
            --bg: #F7F7F5;
            --card: #FFFFFF;
            --text: #37352F;
            --text-sub: #9B9A97;
            --border: #E3E3E0;
            --accent: #2EAADC;
            --accent-hover: #2898C4;
            --hover-bg: #F1F1EF;
            --flag-bg: #FDF4DC;
            --flag-text: #8A6116;
        }
        * { box-sizing: border-box; }
        body {
            margin: 0;
            background: var(--bg);
            color: var(--text);
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "Malgun Gothic", sans-serif;
            font-size: 16px;
            line-height: 1.6;
        }
        .wrap {
            max-width: 720px;
            margin: 0 auto;
            padding: 40px 24px 80px;
        }
        .backlink {
            display: inline-block;
            margin-bottom: 24px;
            color: var(--text-sub);
            font-size: 14px;
            text-decoration: none;
        }
        .backlink:hover { color: var(--accent); }
        h1 {
            font-size: 30px;
            font-weight: 700;
            line-height: 1.2;
            margin: 0 0 8px;
        }
        .subtitle {
            color: var(--text-sub);
            font-size: 14px;
            margin: 0 0 32px;
        }
        .intro {
            background: var(--card);
            border: 1px solid var(--border);
            border-radius: 8px;
            padding: 20px 24px;
            margin-bottom: 32px;
        }
        .intro p { margin: 0; }
        h2 {
            font-size: 20px;
            font-weight: 600;
            line-height: 1.3;
            margin: 40px 0 12px;
            padding-bottom: 8px;
            border-bottom: 1px solid var(--border);
        }
        h3 {
            font-size: 16px;
            font-weight: 600;
            margin: 20px 0 8px;
        }
        p { margin: 0 0 12px; }
        ul, ol {
            margin: 0 0 12px;
            padding-left: 20px;
        }
        li { margin-bottom: 4px; }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 12px 0 20px;
            font-size: 14px;
        }
        caption {
            text-align: left;
            font-size: 13px;
            color: var(--text-sub);
            margin-bottom: 6px;
            caption-side: top;
        }
        th, td {
            border: 1px solid var(--border);
            padding: 8px 12px;
            text-align: left;
            vertical-align: top;
        }
        th {
            background: var(--hover-bg);
            font-weight: 600;
            color: var(--text);
        }
        td.sub { color: var(--text-sub); }
        .flag {
            display: inline-block;
            background: var(--flag-bg);
            color: var(--flag-text);
            font-size: 12px;
            font-weight: 600;
            padding: 2px 8px;
            border-radius: 4px;
            white-space: nowrap;
        }
        .flag-block {
            background: var(--flag-bg);
            color: var(--flag-text);
            border-radius: 6px;
            padding: 10px 14px;
            font-size: 13px;
            margin: 8px 0 16px;
        }
        .flag-block b { display: block; margin-bottom: 4px; }
        a { color: var(--accent); text-decoration: none; }
        a:hover { text-decoration: underline; }
        .meta {
            margin-top: 48px;
            padding-top: 16px;
            border-top: 1px solid var(--border);
            color: var(--text-sub);
            font-size: 13px;
        }
    </style>
</head>

<body>
<div class="wrap">

    <a class="backlink" href="index">&larr; Groupware 9로 돌아가기</a>

    <h1>개인정보 처리방침</h1>
    <p class="subtitle">시행일자: <span class="flag">사업자 확인 필요</span> &nbsp;|&nbsp; 버전 1.0</p>

    <div class="intro">
        <p>
            <span class="flag">[회사명/조직명 입력]</span>(이하 "회사")은 「개인정보 보호법」 등 관련 법령을 준수하며,
            이용자(임직원)의 개인정보를 안전하게 처리하기 위하여 다음과 같이 개인정보 처리방침을 수립·공개합니다.
            본 처리방침은 회사가 제공하는 사내 그룹웨어 서비스(일정관리, 전자결재, 게시판, 사내메일 등)에 적용됩니다.
        </p>
    </div>

    <h2>1. 개인정보의 처리 목적</h2>
    <p>회사는 다음의 목적을 위하여 개인정보를 처리합니다. 처리하는 개인정보는 아래 목적 이외의 용도로는 이용되지 않으며,
        이용 목적이 변경되는 경우에는 관련 법령에 따라 별도의 동의를 받는 등 필요한 조치를 이행할 예정입니다.</p>
    <ul>
        <li>임직원(회원) 식별 및 로그인 인증, 관리자/일반 사용자 권한 구분</li>
        <li>조직(부서·직급) 관리 및 조직도 제공</li>
        <li>사내 일정관리 서비스 제공(일정 등록·조회·수정·삭제)</li>
        <li>사내 게시판 서비스 제공(게시글·댓글 작성, 조회, 좋아요, 첨부파일 관리)</li>
        <li>전자결재 서비스 제공(기안, 결재선 지정, 결재/반려 처리)</li>
        <li>사내 메일 서비스 제공(메일 작성·발송, 외부 메일 계정 연동을 통한 수신 메일 가져오기)</li>
        <li>프로필 사진 등 첨부파일의 등록·조회 서비스 제공</li>
    </ul>

    <h2>2. 처리하는 개인정보의 항목</h2>
    <table>
        <caption>계정(회원) 정보 — 회원가입 및 서비스 이용을 위해 수집</caption>
        <tr><th>구분</th><th>항목</th></tr>
        <tr><td>필수</td><td>아이디, 비밀번호(암호화하여 저장), 이름, 소속부서, 직급, 이용 권한(일반/관리자)</td></tr>
        <tr><td>선택</td><td>프로필 사진</td></tr>
        <tr><td>자동 생성</td><td>로그인·로그아웃 일시</td></tr>
    </table>

    <table>
        <caption>서비스 이용 과정에서 생성·저장되는 정보</caption>
        <tr><th>서비스</th><th>항목</th></tr>
        <tr><td>일정관리</td><td>일정 제목, 일시, 반복 설정, 공개 여부, 내용</td></tr>
        <tr><td>게시판</td><td>게시글·댓글 제목/내용, 작성자, 작성일시, 좋아요·조회 이력, 첨부파일</td></tr>
        <tr><td>전자결재</td><td>기안 문서 제목/내용, 결재선(결재자 정보), 결재의견, 결재일시, 첨부파일</td></tr>
        <tr><td>사내 메일</td><td>메일 제목·본문, 발신/수신 주소, 수발신 일시, 첨부파일</td></tr>
        <tr><td>외부 메일 연동(선택 기능)</td><td>이용자가 직접 입력하는 외부 메일 서버 주소, 계정, 비밀번호</td></tr>
    </table>
    <div class="flag-block">
        <b>법무 검토 필요</b>
        외부 메일 연동 시 입력하는 계정 정보는 이용자가 자신이 소유한 제3자(포털/사내 메일 등) 서비스 접속정보를 스스로 입력·저장하는 구조입니다.
        이를 통상적인 "개인정보 수집 항목"과 동일하게 안내하는 것이 적절한지, 별도 고지·동의가 필요한지 법무 검토가 필요합니다.
    </div>

    <h2>3. 개인정보의 처리 및 보유기간</h2>
    <p>회사는 법령에 따른 개인정보 보유·이용기간 또는 정보주체로부터 개인정보를 수집 시에 동의받은 개인정보 보유·이용기간 내에서
        개인정보를 처리·보유합니다.</p>
    <div class="flag-block">
        <b>사업자 확인 필요</b>
        각 항목별 구체적인 보유기간(예: 퇴직 후 계정정보 보유기간, 게시글·메일·결재문서의 보유기간, 첨부파일 보유기간)은
        현재 코드/DB 조사만으로 확정할 수 없어 사업자 확인이 필요합니다. 확인되는 대로 본 항목에 구체적인 기간을 명시해야 합니다.
    </div>

    <h2>4. 개인정보의 파기절차 및 방법</h2>
    <p>회사는 개인정보 보유기간의 경과, 처리 목적 달성 등 개인정보가 불필요하게 되었을 때에는 지체 없이 해당 개인정보를 파기합니다.</p>
    <div class="flag-block">
        <b>사업자 확인 필요 / 개발 확인 필요</b>
        현재 시스템 구현을 확인한 결과, 회원(계정)·부서·메일계정 등 삭제 처리는 데이터베이스에서 실제로 행을 삭제하지 않고
        "삭제 표시(소프트 삭제)"만 수행하는 것으로 확인되었습니다. 별도의 물리적 파기(하드 삭제) 절차는 코드상 확인되지 않았습니다.
        보유기간이 확정되면 이에 맞춰 실제 파기 절차를 구현하거나, 파기 절차를 담당 부서가 수행하는 방식으로 운영 방안을 정해야 합니다.
    </div>

    <h2>5. 개인정보의 제3자 제공</h2>
    <p>회사는 정보주체의 개인정보를 본 방침에서 명시한 처리 목적 범위 내에서만 처리하며, 정보주체의 동의, 법률의 특별한 규정 등
        「개인정보 보호법」 제17조 및 제18조에 해당하는 경우를 제외하고는 원칙적으로 정보주체의 개인정보를 제3자에게 제공하지 않습니다.</p>
    <p class="subtitle" style="margin:0">(코드 조사 결과, 외부 업체·서비스로 개인정보를 전달하는 연동 로직은 확인되지 않았습니다.)</p>

    <h2>6. 개인정보 처리의 위탁</h2>
    <p>회사는 서비스 운영을 위하여 개인정보 처리업무를 외부에 위탁하지 않고 직접 처리하고 있습니다.</p>
    <div class="flag-block">
        <b>사업자 확인 필요</b>
        실제 운영 인프라(서버 호스팅사, 백업 대행 등)를 외부 사업자에 위탁하는 경우 그 수탁자와 위탁업무 내용을 이 항목에 추가해야 합니다.
        코드 조사만으로는 인프라 운영 주체를 확인할 수 없습니다.
    </div>

    <h2>7. 개인정보의 국외 이전</h2>
    <p>회사 시스템은 개인정보를 국외의 서버로 저장·처리하도록 강제하는 기능을 포함하고 있지 않습니다.</p>
    <div class="flag-block">
        <b>법무 검토 필요</b>
        다만 이용자가 6항의 외부 메일 연동 기능을 이용하여 국외에 소재한 메일 서버(예: 해외 포털 메일)를 직접 등록하는 경우,
        이용자의 선택에 따라 개인정보(메일 계정 정보 및 메일 내용)가 국외로 전송될 수 있습니다.
        이 경우가 「개인정보 보호법」상 "국외 이전"에 해당하는지, 별도 고지가 필요한지 법무 검토가 필요합니다.
    </div>

    <h2>8. 개인정보의 안전성 확보조치</h2>
    <p>회사는 개인정보의 안전성 확보를 위해 다음과 같은 조치를 취하고 있습니다.</p>
    <ul>
        <li><b>접근 권한 관리:</b> 일반 사용자와 관리자 권한을 구분하여 관리자 전용 기능에 대한 접근을 제한합니다.</li>
        <li><b>접근 통제:</b> 로그인 세션이 없는 경우 서비스 화면 접근을 차단하고 로그인 화면으로 이동시킵니다.</li>
        <li><b>비밀번호 관리:</b> 회원 비밀번호는 암호화하여 저장하며, 회원 본인만 변경할 수 있습니다.</li>
    </ul>
    <div class="flag-block">
        <b>개발 개선사항 (아래 "개발 개선사항" 별도 안내 참고)</b>
        안전성 확보조치 수준을 높이기 위해 개선이 필요한 사항들이 발견되었습니다. 자세한 내용은 개발팀에 전달된
        기술 감사 결과를 참고해 주세요.
    </div>

    <h2>9. 정보주체와 법정대리인의 권리·의무 및 행사방법</h2>
    <p>정보주체는 회사에 대해 언제든지 다음 각 호의 개인정보 보호 관련 권리를 행사할 수 있습니다.</p>
    <ul>
        <li>개인정보 열람 요구</li>
        <li>오류 등이 있을 경우 정정 요구</li>
        <li>삭제 요구</li>
        <li>처리정지 요구</li>
    </ul>
    <div class="flag-block">
        <b>사업자 확인 필요</b>
        위 권리 행사는 회사에 대해 <span class="flag">[요청 접수 방법/서식/연락처 입력]</span>을 통하여 하실 수 있으며,
        회사는 이에 대해 지체 없이 조치하겠습니다. 현재 시스템에는 "내 정보" 화면에서 이름·프로필 사진·비밀번호를
        본인이 직접 수정하는 기능만 확인되며, 별도의 열람·삭제 요청 접수 절차는 코드상 확인되지 않아 운영 절차를 정해야 합니다.
    </div>

    <h2>10. 개인정보 보호책임자</h2>
    <p>회사는 개인정보 처리에 관한 업무를 총괄해서 책임지고, 개인정보 처리와 관련한 정보주체의 불만처리 및 피해구제 등을
        위하여 아래와 같이 개인정보 보호책임자를 지정하고 있습니다.</p>
    <table>
        <tr><th style="width:120px">개인정보 보호책임자</th><td><span class="flag">사업자 확인 필요</span></td></tr>
        <tr><td class="sub">성명</td><td><span class="flag">입력 필요</span></td></tr>
        <tr><td class="sub">직책</td><td><span class="flag">입력 필요</span></td></tr>
        <tr><td class="sub">연락처</td><td><span class="flag">입력 필요</span></td></tr>
    </table>

    <h2>11. 개인정보의 열람청구</h2>
    <p>정보주체는 「개인정보 보호법」 제35조에 따른 개인정보의 열람 청구를 아래의 부서에 할 수 있습니다.</p>
    <table>
        <tr><th style="width:120px">개인정보 열람청구 접수·처리 부서</th><td><span class="flag">사업자 확인 필요</span></td></tr>
    </table>

    <h2>12. 권익침해 구제방법</h2>
    <p>정보주체는 개인정보 침해로 인한 구제를 받기 위하여 개인정보분쟁조정위원회, 한국인터넷진흥원 개인정보침해신고센터 등에
        분쟁해결이나 상담 등을 신청할 수 있습니다. 이 밖에 기타 개인정보침해의 신고, 상담에 대하여는 아래의 기관에
        문의하시기 바랍니다.</p>
    <ul>
        <li>개인정보분쟁조정위원회 : (국번없이) 1833-6972 (<a href="https://www.kopico.go.kr" target="_blank" rel="noopener">www.kopico.go.kr</a>)</li>
        <li>개인정보침해신고센터(한국인터넷진흥원 운영) : (국번없이) 118 (<a href="https://privacy.kisa.or.kr" target="_blank" rel="noopener">privacy.kisa.or.kr</a>)</li>
        <li>대검찰청 사이버수사과 : (국번없이) 1301 (<a href="https://www.spo.go.kr" target="_blank" rel="noopener">www.spo.go.kr</a>)</li>
        <li>경찰청 사이버수사국 : (국번없이) 182 (<a href="https://ecrm.cyber.go.kr" target="_blank" rel="noopener">ecrm.cyber.go.kr</a>)</li>
    </ul>
    <p>「개인정보 보호법」 제35조(개인정보의 열람), 제36조(개인정보의 정정·삭제), 제37조(개인정보의 처리정지 등)의 규정에 의한
        요구에 대하여 공공기관의 장이 행한 처분 또는 부작위로 인하여 권리 또는 이익을 침해받은 자는 행정심판법이 정하는 바에
        따라 행정심판을 청구할 수 있습니다.</p>

    <h2>13. 쿠키(Cookie) 등 자동 수집 장치의 설치·운영 및 거부</h2>
    <p>회사는 이용자에게 편리한 서비스 제공을 위해 다음과 같은 쿠키 및 세션을 사용합니다.</p>
    <ul>
        <li><b>로그인 세션(JSESSIONID):</b> 로그인 상태 유지를 위해 사용되며, 브라우저 종료 시 만료됩니다.</li>
        <li><b>아이디 저장 쿠키(sid):</b> 로그인 화면에서 "아이디 저장"을 선택한 경우, 다음 로그인 시 아이디 입력을 돕기 위해 최대 30일간 브라우저에 저장됩니다.</li>
    </ul>
    <p>이용자는 웹브라우저의 설정을 통해 쿠키 저장을 거부하거나 삭제할 수 있습니다. 다만 쿠키 저장을 거부할 경우 "아이디 저장"
        등 일부 편의 기능 이용에 어려움이 있을 수 있습니다.</p>

    <h2>14. 개인정보 처리방침의 변경</h2>
    <p>이 개인정보 처리방침은 시행일자로부터 적용되며, 법령 및 방침에 따른 변경내용의 추가, 삭제 및 정정이 있는 경우에는
        변경사항의 시행 <span class="flag">[통지 시점 입력]</span> 전부터 공지사항을 통하여 고지할 것입니다.</p>

    <div class="meta">
        <p>공고일자: <span class="flag">입력 필요</span> &nbsp;|&nbsp; 시행일자: <span class="flag">입력 필요</span></p>
        <p>본 문서는 실제 서비스 운영 정보(보유기간, 보호책임자, 위탁 현황 등)가 확정되지 않은 초안입니다.
            노란색으로 표시된 항목은 게시 전 사업자 확인 또는 법무 검토가 필요합니다.</p>
    </div>

</div>
</body>
</html>
