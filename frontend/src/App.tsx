import './App.css'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import SignIn from './components/users/SignIn'
import Home from './components/credentials/Home'
import EditCredential from './components/credentials/EditCredential'
import PrivateRoute from './utils/PrivateRoute'
import PersonalProfile from './components/users/PersonalProfile'

function App() {

  return (
    <>
      <BrowserRouter>
      <Routes>
        <Route path='/' element={<SignIn/>}></Route> 
        <Route path='/home' element={<PrivateRoute element={<Home />} />} />
        <Route path='/home/:id' element={<PrivateRoute element={<EditCredential onClose={() => {}} />} />} />
        <Route path='/personal-profile' element={<PersonalProfile/>}></Route>  
      </Routes> 
      </BrowserRouter>   
    </>
  )
}

export default App;
