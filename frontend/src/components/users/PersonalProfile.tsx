import { useUserData } from "../../utils/useUserData"
import "../../styles/users/PersonalProfile.css";
import { FaPen, FaUserCircle } from "react-icons/fa";
import { useRef, useState } from "react";
import UpdateDatas from "./UpdateDatas";
import Modal from "./Modal";
import axios from "axios";

function PersonalProfile() {
    const {userName, userEmail, imageUser} = useUserData();
    const API_URL = import.meta.env.VITE_API_URL;
    const [showOptions, setShowOptions] = useState(false);
    const fileInputRef = useRef<HTMLInputElement>(null);
    const[isUpdateDatasOpen, setIsUpdateDatasOpen] = useState(false);

    const handleImageClick = () => {
        setShowOptions((prev) => !prev);
    };

    const handleUploadClick = () => {
        fileInputRef.current?.click();
    };

   const handleFileChange = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (file) {
        const token = sessionStorage.getItem("token");
        if (!token) return;

        const formData = new FormData();
        formData.append("imageUser", file);
        formData.append("username", userName); // ou userNameData se quiser permitir edição
        formData.append("email", userEmail);   // idem

        try {
            const response = await axios.put(`${API_URL}/users`, formData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "multipart/form-data"
                }
            });

            const { token: newToken } = response.data;
            if (newToken) {
                sessionStorage.setItem("token", newToken);
            }

            window.location.reload();
        } catch (error) {
            console.error("Error to update image:", error);
        }
    }
};


    const handleRemoveImage = async () => {
        try {
            const token = sessionStorage.getItem("token");
            const response = await axios.delete(`${API_URL}/users/image`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            const {token: newToken} = response.data;
            if(newToken) {
                sessionStorage.setItem("token", newToken);
            }

            window.location.reload();
        }catch(error) {
            console.error("Error to delete image " + error);    
        }
    };


    return (
        <div className='personal-profile'>
            <h2>Informações pessoais</h2>
             <div className="profile-icon" onClick={handleImageClick}>
                    {imageUser ? (
                <img
                  src={`${API_URL}${imageUser}`}
                  alt="Imagem de perfil"
                  className="profile-picture"
                  style={{ width: 200, height: 200, borderRadius: "50%", objectFit: "cover" }}
                />
              ) : (
                <FaUserCircle size={200} />
              )}
              <div className="edit-overlay">
                <FaPen/>
              </div>
            </div>
            {showOptions && (
                    <div className="edit-options">
                        <button onClick={handleUploadClick}>Atualizar imagem</button>
                        <button onClick={handleRemoveImage}>Remover imagem</button>
                        <input
                            type="file"
                            accept="image/*"
                            ref={fileInputRef}
                            style={{ display: "none" }}
                            onChange={handleFileChange}
                        />
                    </div>
                )}
            
            <label>Nome</label>
            <input type='text' value={userName} disabled></input>

            <label>Email</label>
            <input type='email' value={userEmail} disabled></input>

            <label>Senha</label>
            <input type='password' disabled></input>
            
            <div className="div-personal-profile-btn">
                <button className="update-account-btn" onClick={() => setIsUpdateDatasOpen(true)}>
                    Atualizar dados
                </button>
                <Modal isOpen={isUpdateDatasOpen} onClose={() => setIsUpdateDatasOpen(false)}>
                    <UpdateDatas
                        onClose={() => setIsUpdateDatasOpen(false)}
                    />
                </Modal>
                <button className="delete-account-btn">Deletar conta</button>
            </div>
            
        </div>
    )
}

export default PersonalProfile