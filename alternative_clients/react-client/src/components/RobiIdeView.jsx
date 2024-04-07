import { useState } from "react";
import Renderer from './Renderer.jsx'
import CodingView from "./CodingView.jsx";

export default function RobiIdeView() {

    const [renderLoading, setRenderLoading] = useState(false)

    return <div className="container mt-5">
        <CodingView setRenderLoading={setRenderLoading}/>
        <hr />
        <Renderer rendererLoading={renderLoading}/>
    </div>
}