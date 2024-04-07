import ObjectComputer from "./graphics/ObjectComputer.jsx"

export default function Renderer({state}) {

    if(!state.current || !state.current.resp) return <><h1>Espace rendu</h1><p>Aucun rendu</p></>

    return <>
        <h1>Espace rendu</h1>
        <p>
            Feedback: {state.current.resp.feedback}
        </p>
        <ObjectComputer object={state.current.resp.world}/>
    </>
}