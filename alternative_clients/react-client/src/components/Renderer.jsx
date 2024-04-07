export default function Renderer({rendererLoading}) {
    return <>
        {!rendererLoading && <img id="renderer" alt="renderer" />}
        {rendererLoading && <div className="spinner-grow text-primary" role="status">
            <span className="sr-only"></span>
        </div>}
    </>
}