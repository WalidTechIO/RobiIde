import ObjectRenderer from "./graphics/ObjectRenderer.jsx"

export default function Renderer({current}) {

    if(!current) return <p>Aucun rendu</p>

    return <>
        <p>
            Feedback: {current.feedback}
        </p>
        <ObjectRenderer object={current.world} />
    </>
}