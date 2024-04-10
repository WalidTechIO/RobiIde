export default function CodingView({ submitCallback, direct, toggleDirect, setFiles, next, isLast}) {

    const handleSubmit = (e) => {
        e.preventDefault();
        const data = new FormData(e.target);
        submitCallback(data.get("ip"), data.get("port"), data.get("program"))
    }

    return <>
        <h1>Espace code</h1>
        <form onSubmit={handleSubmit}>
            <div className="mb-3">
                <label htmlFor="ip" className="form-label">Adresse du serveur:</label>
                <input type="text" className="form-control" id="ip" name="ip" />
            </div>
            <div className="mb-3">
                <label htmlFor="port" className="form-label">Port du serveur:</label>
                <input type="text" className="form-control" id="port" name="port"/>
            </div>
            <div className="mb-3">
                <label htmlFor="mode" className="form-check-label">Execution directe: </label>
                <input type="checkbox" className="form-check-input mx-1" id="mode" name="mode" checked={direct} onChange={toggleDirect} />
            </div>
            <div className="mb-3">
                <label htmlFor="program" className="form-label">Programme robi:</label>
                <textarea className="form-control" id="program" name="program"></textarea>
            </div>
            <div className="mb-3">
                <label htmlFor="selector" className="form-label">Images:</label>
                <input className="form-control" type="file" id="selector" multiple={true} onChange={(e) => setFiles(e.target.files)}/>
            </div>
            <div>
                <button className="btn btn-primary mx-1" type="submit">{direct ? "Lancer l'animation" : "Compiler le programme"}</button>
                {!direct && <button type="button" className="btn btn-dark mx-1" onClick={next}>{!isLast ? "Executer la prochaine instruction" : "Revenir au début"}</button>}
            </div>
        </form>
    </>
}