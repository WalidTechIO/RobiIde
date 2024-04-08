import ObjectRenderer from "./graphics/ObjectRenderer.jsx"

export default function Renderer({state}) {

    if(!state.current || !state.current.resp) return <><h1>Espace rendu</h1><p>Aucun rendu</p></>

    return <>
        <h1>Espace rendu</h1>
        <p>
            Feedback: {state.current.resp.feedback}
        </p>
        <ObjectRenderer object={state.current.resp.world} images={state.files}/>
    </>
}