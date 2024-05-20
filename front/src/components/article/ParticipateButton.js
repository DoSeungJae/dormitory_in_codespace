import { Button } from "@mui/material";

function ParticipateButton() {
    const token = localStorage.getItem("token");
    const participate = async ()  => {
        console.log(1);

    }

    return (
        <Button 
            variant="contained"
            size="small"
            onClick={participate}
            sx={{ backgroundColor: '#FF8C00', '&:hover': { backgroundColor: '#FF8C00' } }}
        >
            참여
        </Button>
    );
}

export default ParticipateButton;
