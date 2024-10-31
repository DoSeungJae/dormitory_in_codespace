import dayjs from 'dayjs';

export const getRelativeTime = (absTime) => {
    console.log(absTime);
    if(Array.isArray(absTime)===true){
        absTime=arrayToLocalDateTimeString(absTime);
    }
    const now=dayjs();
    const created=dayjs(absTime);  

    const diffInSeconds = now.diff(created, 'second');
    const diffInMinutes = now.diff(created, 'minute');
    const diffInHours = now.diff(created, 'hour');
    const diffInDays = now.diff(created, 'day');
  
    if (diffInSeconds < 60) {
      return `${diffInSeconds}초 전`;
    } else if (diffInMinutes < 60) {
      return `${diffInMinutes}분 전`;
    } else if (diffInHours < 24) {
      return `${diffInHours}시간 전`;
    } else if (diffInDays === 1) {
      return '하루 전';
    } else if (diffInDays === 2) {
      return '이틀 전';
    } else if (diffInDays < 365) {
      return created.format('MM월 DD일');
    } else {
      return created.format('YYYY년 MM월 DD일');
    }

}


const arrayToLocalDateTimeString = (timeArray) => {
    const [year, month, day, hour, minute, second, millisecond] = timeArray;
    const date = new Date(year, month - 1, day, hour, minute, second, millisecond || 0);
  
    let isoString = date.toISOString();
  
    if (timeArray.length === 6) {
      isoString = isoString.replace('Z', '.0Z');
    }
  
    const localDateTimeString = isoString.split('.')[0] + (timeArray.length === 7 ? `.${millisecond}` : '.0');
    return localDateTimeString;
  }