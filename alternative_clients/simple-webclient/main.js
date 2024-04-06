//Ajout de la methode convertToBase64 a la classe File de Javascript
File.prototype.convertToBase64 = function (callback) {
    var reader = new FileReader();
    reader.onloadend = function (e) {
        callback(e.target.result, e.target.error);
    };
    reader.readAsDataURL(this);
}

//Execute l'animation sur la page repondant au programme et executer sur le server ip:port (necessite image d'id 'renderer')
const fetchScript = (ip, port, programFinal) => {
    if (document.getElementById("animationScript")) document.head.removeChild(document.getElementById("animationScript"));
    fetch(`http://${ip}:${port}/endpoint`, { method: "POST", body: JSON.stringify(programFinal) })
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

/*
A la soumission du formulaire on pre-compile le programme 
grace au file selector et a la methode convertToBase64 puis on execute l'animations
*/
const handleSubmit = (e) => {
    e.preventDefault();
    const data = new FormData(e.target);
    const filesArray = document.getElementById("selector").files ?? []

    const computeProgram = new Promise((resolve, reject) => {
        let program = data.get("program")
        let fileRemaining = filesArray.length
        if(fileRemaining === 0) {
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

document.getElementById("form").addEventListener("submit", handleSubmit)