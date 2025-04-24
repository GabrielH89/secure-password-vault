import axios from "axios";
import { useEffect, useState } from "react"

export interface UserData {
    username: string,
    email: string;
    imageUser: string;
}

export const useUserData = () => {
    const [userName, setUserName] = useState("");
    const [userEmail, setUserEmail] = useState("");
    const [imageUser, setUserImage] = useState("");

    useEffect(() => {
        const fetchUser = async () => {
            try{
                const token = sessionStorage.getItem("token");
                const response = await axios.get("http://localhost:8080/users", {
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                });
                const {username, email, imageUser} = response.data;
                setUserName(username);
                setUserEmail(email);
                setUserImage(imageUser);
            }catch(error) {
                console.log("Error " + error)
            }
        }
        fetchUser();
    }, []);

    return {userName, userEmail, imageUser};
}