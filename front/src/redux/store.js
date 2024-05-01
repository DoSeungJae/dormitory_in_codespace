import { configureStore } from "@reduxjs/toolkit";

const initialState={
    selectComponentIndex: 0,
};
export default configureStore({
    reducer:{
        home:(state=initialState) => state,
    },
});