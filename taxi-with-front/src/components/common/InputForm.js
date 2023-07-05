import React from 'react'
import 'bootstrap/dist/css/bootstrap.css'; 
import 'bootstrap/dist/js/bootstrap.js'; 

const InputForm = ({type,placeholder,value}) => {
    return (
        <input 
        type={type} //text or password
        class="form-control" 
        placeholder={placeholder} 
        value={value}

        />
    );
};

export default InputForm;


//인폿 폼을 label 안에 넣어 작성하는 것이 관례임 - ChatGPT`