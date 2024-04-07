export default function Renderer({rendererLoading}) {
    return <>
        <h1>Espace rendu</h1>
        {!rendererLoading && <img id="renderer" alt="renderer" className="mb-2 img-fluid"/>}
        {rendererLoading && <div className="spinner-grow text-primary" role="status">
            <span className="sr-only"></span>
        </div>}
    </>
}