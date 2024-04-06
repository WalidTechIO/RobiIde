import {useState} from "react";

export default function CodingView() {

    const [ip, setIp] = useState("")
    const [port, setPort] = useState ("")

    const fetchScript = (programFinal) => {
        if(document.getElementById("animationScript")) document.head.removeChild(document.getElementById("animationScript"));
        fetch(`http://${ip}:${port}/endpoint`, {method: "POST", body: JSON.stringify(programFinal)})
            .then(rep => rep.text()
                .then(t => {
            return new DOMParser().parseFromString(t, "text/html");
        }))
            .then(dom => {
            const animation = dom.getElementById('animationScript')
            const script = document.createElement('script')
            script.defer = true
            script.id = "animationScript"
            script.textContent = animation.textContent
            document.head.appendChild(script)
        }).catch(error => console.log("Error while fetching animation: " + error))
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        const data = new FormData(e.target)
        fetchScript({
            type: "PROG",
            program: {
                mode: "DIRECT",
                contenu: data.get("program")
            }
        })
    }

    return <>
        <p>Note: Ce client ne gere pas toutes les images car le rendu est effectue cote serveur. Vous pouvez voir les images disponibles sur le serveur à l'adresse http://{ip === '' ? "adresse" : ip}:{port === '' ? "port" : port}/images/list</p>
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
                <label htmlFor="port" className="form-label">Programme robi:</label>
                <textarea className="form-control" id="program" name="program"></textarea>
            </div>
            <button className="btn btn-primary" type="submit">{"Executer l'animation"}</button>
        </form>
    </>
}