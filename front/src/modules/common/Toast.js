import React from "react";
import {ToastContainer} from 'react-toastify';
import styled from "styled-components";

const brightOrange=`#FFA07A;`;
export const StyledToastContainer=styled(ToastContainer)`
    padding-left:2vw;
    padding-right:2vw;
    margin-top:3vh;
  &&&.Toastify__toast-container {
  }
  .Toastify__toast {
    //background-color: ${brightOrange};

  }
  .Toastify__toast-body {
  }
  .Toastify__progress-bar {
  }
`;