import axios from "axios";
import { useEffect, useState } from "react"

export interface UserData {
    username: string,
    email: string;
}

export const useUserData = () => {
    const [userName, setUserName] = useState("");
    const [userEmail, setUserEmail] = useState("");

    useEffect(() => {
        const fetchUser = async () => {
            try{
                const token = sessionStorage.getItem("token");
                const response = await axios.get("http://localhost:8080/users", {
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                });
                const {username, email} = response.data;
                setUserName(username);
                setUserEmail(email);
            }catch(error) {
                console.log("Error " + error)
            }
        }
        fetchUser();
    }, []);

    return {userName, userEmail};
}