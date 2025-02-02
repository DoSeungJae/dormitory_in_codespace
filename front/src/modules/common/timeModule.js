import dayjs from 'dayjs';

export const getRelativeTime = (absTime) => {
    if(Array.isArray(absTime)===true){
        absTime=arrayToLocalDateTimeString(absTime);
    }
    const abs = (x) => {return x>0?x:-x};
    const now = dayjs();
    const created = dayjs(absTime);  
    const diffInSeconds = abs(now.diff(created, 'second'));
    const diffInMinutes = abs(now.diff(created, 'minute'));
    const diffInHours = abs(now.diff(created, 'hour'));
    const diffInDays = abs(now.diff(created, 'day'));
    
    const beforeAfter = now>created?"전":"후";

    if (diffInSeconds < 60) {
      return `${diffInSeconds}초 ${beforeAfter}`;
    } else if (diffInMinutes < 60) {
      return `${diffInMinutes}분 ${beforeAfter}`;
    } else if (diffInHours < 24) {
      return `${diffInHours}시간 ${beforeAfter}`;
    } else if (diffInDays === 1) {
      return `하루 ${beforeAfter}`;
    } else if (diffInDays === 2) {
      return `이틀 ${beforeAfter}`;
    } else if (created.get("year") === now.get("year")) {
      return created.format('MM월 DD일');
    } else {
      return created.format('YYYY년 MM월 DD일');
    }

}

export const latterTime = (time1, time2) => {
  const t1 = dayjs(time1);
  const t2 = dayjs(time2);
  return time1 >= time2 ? time1 : time2;
}


const arrayToLocalDateTimeString = (timeArray) => {
  const [year, month, day, hour, minute, second, millisecond] = timeArray;
  
  let truncatedMillisecond = millisecond;
  if (millisecond && millisecond.toString().length > 3) {
    truncatedMillisecond = parseInt(millisecond.toString().slice(0, 3), 10);
  }
  
  const date = new Date(year, month - 1, day, hour, minute, second, truncatedMillisecond || 0);
  
  // ISO 형식으로 변환
  let isoString = date.toISOString();
  
  // 밀리초가 없는 경우 .000 추가
  if (timeArray.length === 6) {
    isoString = isoString.replace('Z', '.000Z');
  } else {
    // 밀리초가 있는 경우, 자른 밀리초로 대체
    isoString = isoString.replace(/\.\d{3}Z$/, `.${truncatedMillisecond.toString().padStart(3, '0')}Z`);
  }
  
  return isoString;
};