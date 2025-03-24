import './App.css'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import SignIn from './components/users/SignIn'
import Home from './components/credentials/Home'

function App() {

  return (
    <>
      <BrowserRouter>
      <Routes>
        <Route path='/' element={<SignIn/>}></Route> 
        <Route path='/home' element={<Home/>}></Route> 
      </Routes> 
      </BrowserRouter>   
    </>
  )
}

export default App;
