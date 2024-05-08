
import axios from 'axios';
import { toast } from 'react-toastify';

export const goArticlePage = async (article,dorId,
                                    isEndPage,page,token
                                    ,setSelectComponentIndex) => {
    
    
    try {
      const response = await axios.get('http://localhost:8080/api/v1/article/validate', {
          headers: {
              'Authorization': `${token}`
          }
      });

      if (response.data === true) {
        localStorage.setItem('article',JSON.stringify(article));
        setSelectComponentIndex(5);
      } else {
        setSelectComponentIndex(8);
        localStorage.setItem("nextIndex",5);
        toast.error('글을 보기 위해선 로그인이 필요해요!');
        
      }
  } catch (error) {
      console.error('An error occurred:', error);
  }
    
  };


export function calculateDorItemStyle(selectedIdx, idx) {
    const isSelected = idx === selectedIdx;
    let color;
    const item = ['오름1', '오름2', '오름3', '푸름1', '푸름2', '푸름3', '푸름4'][idx];
    if (item.startsWith('오름')) {
      color = `hsl(120, 39%, ${55 - (idx * 6)}%)`;
    } else {
      color = `hsl(197, 71%, ${70 - (idx - 3) * 10}%)`;
    }
    const border = `2px solid ${color}`;
    return {
      background: isSelected ? color : 'none',
      color: isSelected ? '#fff' : 'black',
      border: border,
    };
  }

export const dorIdToDorName={
    1:"오름1",
    2:"오름2",
    3:"오름3",
    4:"푸름1",
    5:"푸름2",
    6:"푸름3",
    7:"푸름4"
  };
