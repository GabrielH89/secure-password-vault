import { useUserData } from "../../utils/useUserData"
import "../../styles/users/PersonalProfile.css";
import { FaPen, FaUserCircle } from "react-icons/fa";
import { useRef, useState } from "react";

function PersonalProfile() {
    const {userName, userEmail, imageUser} = useUserData();
    const API_URL = import.meta.env.VITE_API_URL;
    const [showOptions, setShowOptions] = useState(false);
    const fileInputRef = useRef<HTMLInputElement>(null);

    const handleImageClick = () => {
        setShowOptions((prev) => !prev);
    };

    const handleUploadClick = () => {
        fileInputRef.current?.click();
    };

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0];
        if (file) {
            // TODO: Enviar o arquivo para o backend
            console.log("Upload de imagem:", file);
        }
    };

    const handleRemoveImage = () => {
        // TODO: Chamar API para remover imagem
        console.log("Imagem removida");
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

            <button className="delete-account-btn">Deletar conta</button>
        </div>
    )
}

export default PersonalProfile