// 날자 포맷 모듈 생성

/*  1. dateString을 매개변수로 전달받아 format을
       다 셋팅한 후 리턴하는 구조로 작성
*/ 
export const formatDate = (dateString) => { 
    // 2. date 객체 생성
    const date = new Date(dateString) 

    // 3. 옵션 셋팅 
    // (1번째 방법 : 포맷 option으로 날짜 형식 지정하기)
    /*
        const options = {
            year: 'numeric', month: 'numeric', day: 'numeric', 
            hour: 'numeric', minute: 'numeric', second: 'numeric', 
            hour12: true, timeZone: 'Asia/Seoul'
        };
        return date.toLocaleString('ko-KR', options)
    */

    // 2번째 방법 : 직접 포맷 형식 지정하기
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}