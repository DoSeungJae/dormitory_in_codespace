
import axios from 'axios';
import { toast } from 'react-toastify';
import Swal from 'sweetalert2';

export const goArticlePage = async (articlePreview,dorId,isEndPage,page,token,setSelectComponentIndex ,selectComponentIndex, setArticle) => {
    
    try {
      const response = await axios.get('https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/article/validate', {
          headers: {
              'Authorization': `${token}`
          }
      });
      const isArticlePath=`https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/article/isArticle/${articlePreview.id}`;
      const isArticleResponse=await axios.get(isArticlePath);
      const isArticle=isArticleResponse.data;
      if(!isArticle){
        Swal.fire({
          text:"해당 글을 찾을 수 없어요.",
          confirmButtonColor:"#FF8C00"
        }).then((result)=>{
          if(result.isConfirmed){
            window.location.reload();
            localStorage.setItem("index",selectComponentIndex);
          }
        });
        return ;
      }
      if (response.data === true) {
        //localStorage.setItem('article',JSON.stringify(articlePreview)); //이 과정은 더이상 필요 없음. article을 가져와 articlePage에 set하는 것이 중요함.

        try{
          const articlePath=`https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/article/${articlePreview.id}`;
          const response=await axios.get(articlePath);
          setArticle(response.data);
        }catch(error){
          console.error("에러 : ",error);
        }

        //setArticle을 하면 ArticlePage 컴포넌트가 렌더링 돼 정보를 set한 후 ArticlePage로 이동하기.
        //ArticlePage 내 initPage 함수 다시 작성할 필요 있음.

        localStorage.setItem("nextIndex",selectComponentIndex);
        setSelectComponentIndex(5);

      } else {
        setSelectComponentIndex(8);
        localStorage.setItem("nextIndex",5);
        localStorage.setItem("articleId",articlePreview.id);
        toast.error('글을 보기 위해선 로그인이 필요해요!');
        
      }
  } catch (error) {
      console.error('An error occurred:', error);
  }
    
  };

export const goToPostingPage = async (selectComponentIndex,setSelectComponentIndex) => {
  const token=localStorage.getItem("token");
  try{
    const response = await axios.get('https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/article/validate', {
      headers: {
          'Authorization': `${token}`
      }
  });
    if(response.data===true){
      localStorage.setItem("nextIndex",selectComponentIndex);
      setSelectComponentIndex(2);
    }
    else{
      setSelectComponentIndex(8);
      localStorage.setItem("nextIndex",2);
      toast.error("글을 쓰려면 로그인이 필요해요!");
    }
  }catch(error){
    console.error(error);
  }
}


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
