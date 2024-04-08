import React, {useState} from 'react';

export default function CodingView({robiclient}) {

    const [ip, setIp] = useState("")
    const [port, setPort] = useState("")

    const handleSubmit = (e) => {
        e.preventDefault();
        const data = new FormData(e.target);
        robiclient.fetchData(ip, port, data.get("program"))
    }

    return <>
        <h1>Espace code</h1>
        <form onSubmit={handleSubmit}>
            <div className="mb-3">
                <label htmlFor="ip" className="form-label">Adresse du serveur:</label>
                <input type="text" className="form-control" id="ip" name="ip" value={ip} onChange={(e) => setIp(e.target.value)}/>
            </div>
            <div className="mb-3">
                <label htmlFor="port" className="form-label">Port du serveur:</label>
                <input type="text" className="form-control" id="port" name="port" value={port} onChange={(e) => setPort(e.target.value)}/>
            </div>
            <div className="mb-3">
                <label htmlFor="mode" className="form-check-label">Execution directe: </label>
                <input type="checkbox" className="form-check-input mx-1" id="mode" name="mode" checked={robiclient.state.direct} onChange={(e) => robiclient.setDirect(!robiclient.state.direct)} />
            </div>
            <div className="mb-3">
                <label htmlFor="program" className="form-label">Programme robi:</label>
                <textarea className="form-control" id="program" name="program"></textarea>
            </div>
            <div className="mb-3">
                <label htmlFor="selector" className="form-label">Images:</label>
                <input className="form-control" type="file" id="selector" multiple={true} onChange={(e) => robiclient.setFiles(e.target.files)}/>
            </div>
            <div className="h-stack">
                <button className="btn btn-primary mx-1" type="submit">{robiclient.state.direct ? "Lancer l'animation" : "Transmettre le programme"}</button>
                {!robiclient.state.direct && <button type="button" className="btn btn-dark mx-1" onClick={robiclient.next}>Executer la prochaine instruction</button>}
                {robiclient.reset}
            </div>
        </form>
    </>
}