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
        e.preventDefault();
        const data = new FormData(e.target);
        const filesArray = document.getElementById("selector").files ?? []

        File.prototype.convertToBase64 = function (callback) {
            var reader = new FileReader();
            reader.onloadend = function (e) {
                callback(e.target.result, e.target.error);
            };
            reader.readAsDataURL(this);
        }

        const computeProgram = new Promise((resolve) => {
            let program = data.get("program")
            let fileRemaining = filesArray.length
            if (fileRemaining === 0) {
                resolve(program)
            }
            for (let i = 0; i < filesArray.length; i++) {
                filesArray[i].convertToBase64(function (base64) {
                    program = program.replaceAll("{" + filesArray[i].name + "}", base64.split(',')[1])
                    fileRemaining--
                    if (fileRemaining === 0) {
                        resolve(program)
                    }
                })
            }
        })

        computeProgram.then(program => fetchScript(data.get("ip"), data.get("port"), {
            type: "PROG",
            program: {
                mode: "DIRECT",
                contenu: program
            }
        }))

    }

    return <>
        <p>Note: Pour utiliser une image dans le programme il faut l'ajouter au file selector et utiliser {"{nomDeLimage.extension}"} dans le script comme chemin</p>
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
            <input type="file" id="selector" multiple=""></input>
            <button className="btn btn-primary" type="submit">{"Executer l'animation"}</button>
        </form>
    </>
}