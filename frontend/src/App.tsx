import './App.css'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import SignIn from './components/users/SignIn'
import Home from './components/credentials/Home'
import EditCredential from './components/credentials/EditCredential'
import PrivateRoute from './utils/PrivateRoute'

function App() {

  return (
    <>
      <BrowserRouter>
      <Routes>
        <Route path='/' element={<SignIn/>}></Route> 
        <Route path='/home' element={<PrivateRoute element={<Home />} />} />
        <Route path='/home/:id' element={<PrivateRoute element={<EditCredential onClose={() => {}} />} />} />
      </Routes> 
      </BrowserRouter>   
    </>
  )
}

export default App;
