
import axios from 'axios';

export const goArticlePage = async (article,scrollPosition,dorId,
                                    isEndPage,page,token,navigate) => {

    const saveScrollState = () => {
        localStorage.setItem('scrollPosition',scrollPosition);
        localStorage.setItem('dor',dorId);
        if(isEndPage){
          localStorage.setItem('page',page-1);
        }
        else{
          localStorage.setItem('page',page);
        }
      }
      
    try {
      const response = await axios.get('http://localhost:8080/api/v1/article/validate', {
          headers: {
              'Authorization': `${token}`
          }
      });

      if (response.data === true) {
        saveScrollState();
        navigate('/article',{state:{info:article}})
      } else {
          saveScrollState();
          navigate('/logIn',{state:
            {from:'/',type:"error",
            message:'글을 보기 위해선 로그인이 필요해요!'}
                            });
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