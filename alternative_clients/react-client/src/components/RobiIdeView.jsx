import Renderer from './Renderer.jsx'
import CodingView from "./CodingView.jsx";

export default function RobiIdeView() {
    return <div className="container mt-5">
        <CodingView />
        <hr />
        <Renderer />
    </div>
}