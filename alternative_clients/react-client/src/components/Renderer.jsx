import ObjectRenderer from "./graphics/ObjectRenderer.jsx"

export default function Renderer({state}) {

    if(!state.current || !state.current.resp) return <p>Aucun rendu</p>

    return <>
        <p>
            Feedback: {state.current.resp.feedback}
        </p>
        <ObjectRenderer object={state.current.resp.world} images={state.files}/>
    </>
}