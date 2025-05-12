import { useEffect, useState } from "react";
import { useUserData } from "../../utils/useUserData";
import axios from "axios";
import LoadingModal from "../credentials/LoadingModal";

interface UpdateDatasProps {
    onClose: () => void;
}

function UpdateDatas({onClose}: UpdateDatasProps) {
    const {userName, userEmail} = useUserData();
    const [userNameData, setUserNameData] = useState("");
    const [userEmailData, setUserEmailData] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const API_URL = import.meta.env.VITE_API_URL;

    useEffect(() => {
        setUserNameData(userName);
        setUserEmailData(userEmail);
    }, [userName, userEmail]);

    const updateDatasUser = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setErrorMessage("");

        try{
            const userData = {username: userNameData, email: userEmailData};
            const token = sessionStorage.getItem("token");

            if(!token) {
                throw new Error("Usuário não autenticado");
            }

            const response = await axios.put(`${API_URL}/users`, userData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "multipart/form-data"
                }
            } )

            const {token: newToken} = response.data;
            if(newToken) {
                sessionStorage.setItem("token", newToken);
            }

            window.location.reload();

            onClose();
        }catch(error) {
            setErrorMessage("Erro ao atualizar os dados do usuário");
            console.log("Erro: " + error);
            setIsLoading(false);
        }   
    } 

    return (
        <div>
            {isLoading && <LoadingModal/>}
            <form className="update-datasUser-form" onSubmit={updateDatasUser}>
                <div className="formgroup">
                    <label>Nome</label>
                    <input type="text" value={userNameData} maxLength={255}
                    onChange={(e) => setUserNameData(e.target.value)}
                    ></input>
                </div>
                <div className="formgroup">
                    <label>Email</label>
                    <input type="text" value={userEmailData} maxLength={600}
                    onChange={(e) => setUserEmailData(e.target.value)}
                    ></input>
                </div>

                {errorMessage && <p className="error">{errorMessage}</p>}
                <div className="div-update-btn">
                    <button type="submit" className="update-btn" >Atualizar</button>
                </div>
            </form>
        </div>
    )
}

export default UpdateDatas

