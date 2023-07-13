
import './App.css';
import LogInPage from './routeComponents/logIn/LogInPage';
import {Routes,Route,BrowserRouter} from "react-router-dom";
import SignInPage from './routeComponents/signIn/SignInPage';


function App() {
  

  return (

    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route exact path="/" element={<LogInPage/>}></Route>
          <Route path="/signIn" element={<SignInPage/>}></Route>
        </Routes>
      </BrowserRouter>  
      
    </div>
  );
}



export default App;
