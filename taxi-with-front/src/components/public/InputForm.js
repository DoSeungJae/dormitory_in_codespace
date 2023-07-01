import React from 'react'
import 'bootstrap/dist/css/bootstrap.css'; 
import 'bootstrap/dist/js/bootstrap.js'; 

const InputForm = ({type,placeholder}) => {
    return (
        <input 
        type={type} //text or password
        className="form-control" 
        placeholder={placeholder} 
        />
    );
};

export default InputForm;


//인폿 폼을 label 안에 넣어 작성하는 것이 관례임 - ChatGPT