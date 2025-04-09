import { useUserData } from "../../utils/useUserData"
import "../../styles/users/PersonalProfile.css";

function PersonalProfile() {
    const {userName, userEmail} = useUserData();
        
    return (
        <div className='personal-profile'>
            <h2>Informações pessoais</h2>
            <label>Nome</label>
            <input type='text' value={userName} disabled></input>

            <label>Email</label>
            <input type='email' value={userEmail} disabled></input>

            <button className="delete-account-btn">Deletar conta</button>
        </div>
    )
}

export default PersonalProfile