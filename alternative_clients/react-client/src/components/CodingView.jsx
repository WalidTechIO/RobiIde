import { useState } from "react";

export default function CodingView({robiclient}) {

    const handleSubmit = (e) => {
        e.preventDefault();
        const data = new FormData(e.target);
        robiclient.fetchData(data.get("program"))
    }

    return <>
        <h1>Espace code</h1>
        <form onSubmit={handleSubmit}>
            <div className="mb-3">
                <label htmlFor="ip" className="form-label">Adresse du serveur:</label>
                <input type="text" className="form-control" id="ip" name="ip" value={robiclient.state.ip} onChange={(e) => robiclient.setIp(e.target.value)}/>
            </div>
            <div className="mb-3">
                <label htmlFor="port" className="form-label">Port du serveur:</label>
                <input type="text" className="form-control" id="port" name="port" value={robiclient.state.port} onChange={(e) => robiclient.setPort(e.target.value)}/>
            </div>
            <div className="mb-3">
                <label htmlFor="mode" className="form-check-label mr-1">Execution directe: </label>
                <input type="checkbox" className="form-check-input" id="mode" name="mode" checked={robiclient.state.direct} onChange={(e) => robiclient.setDirect(!robiclient.state.direct)} />
            </div>
            <div className="mb-3">
                <label htmlFor="program" className="form-label">Programme robi:</label>
                <textarea className="form-control" id="program" name="program"></textarea>
            </div>
            <div className="mb-3">
                <label htmlFor="selector" className="form-label mr-1">Images:</label>
                <input type="file" id="selector" multiple={true}/>
            </div>
            <button className="btn btn-primary" type="submit">Envoyer le code ROBI au serveur</button>
        </form>
        <button className="btn mt-2" onClick={robiclient.next}>{robiclient.state.direct ? "Lancer l'animation" : "Executer la prochaine instruction"}</button>
    </>
}