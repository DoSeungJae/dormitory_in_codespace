
import './App.css';
import 'bootstrap/dist/css/bootstrap.css'; 
import 'bootstrap/dist/js/bootstrap.js'; 

function App() {
  return (
    <div className="App">


      <div className="container pb-4  ">
        <div className="row mt-4">

        </div>
        <h1 className="Title">Taxi With</h1>
      </div>

      <div className="container">
      <div className="row mt-5">
        <div className="col">
          <input type="text" className="form-control" placeholder="아이디" />
        </div>
      </div>
      <div className="row mt-3">
        <div className="col">
          <input type="password" className="form-control" placeholder="비밀번호" />
        </div>
      </div>


      <div class="d-grid mt-4 pb-4">
        <button class="btn btn-primary" type="button">로그인</button>
      </div>
    </div>

    <div className="continer border-top">
      <div className="row mt-1">
        <div className="col">회원가입</div>
        <div className="col">아이디 찾기</div>
        <div className="col">비밀번호 찾기</div>
      </div>
    </div>


    </div>
  );
}



export default App;
