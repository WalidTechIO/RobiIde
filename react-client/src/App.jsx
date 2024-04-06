import Renderer from './components/Renderer.jsx'
import CodingView from "./components/CodingView.jsx";

function App() {

  return (
    <main className="flex gap-2">
        <div className="container mt-5">
            <CodingView />
            <hr />
            <Renderer />
        </div>
    </main>
  )
}

export default App
