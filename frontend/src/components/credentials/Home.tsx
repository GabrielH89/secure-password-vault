import { useEffect } from "react"
import axios from "axios";

function Home() {
    useEffect(() => {
        const fetchCredentials = async () => {
            try{
                const token = sessionStorage.getItem('token');
                const response = await axios.get("http://localhost:8080/credentials", {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    }
                });
                console.log(response.data);
            }catch(error) {
                console.log("Error " + error);
            }
        }
        fetchCredentials();
    }, [])

    return (
        <div>Home</div>
    )
}

export default Home