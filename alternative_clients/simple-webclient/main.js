//Ajout de la methode convertToBase64 a la classe File de Javascript
File.prototype.convertToBase64 = function (callback) {
    var reader = new FileReader();
    reader.onloadend = function (e) {
        callback(e.target.result, e.target.error);
    };
    reader.readAsDataURL(this);
}

const renderzone = document.getElementById("renderzone")
const loader = '<div class="mx-auto align-items-center justify-content-center d-flex" style="height: 10vh;"><div class="spinner-grow text-primary" role="status"><span class="sr-only"></span></div></div>'
const renderer = '<img alt="Aucun rendu" id="renderer" class="mb-2 img-fluid"/>'
const modal = '<div id="modal" style="position: absolute; bottom: 0; right: 20px"><div class="alert alert-danger alert-dismissible fade show " role="alert">Error while fetching data<button type="button" class="btn-close"></button></div></div>'

//Execute l'animation sur la page repondant au programme et executer sur le server ip:port (necessite image d'id 'renderer')
const fetchScript = (ip, port, programFinal) => {
    if (document.getElementById("animationScript")) document.head.removeChild(document.getElementById("animationScript"));
    renderzone.innerHTML = loader
    fetch(`http://${ip}:${port}/render`, { method: "POST", body: JSON.stringify(programFinal) })
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
            renderzone.innerHTML = renderer
            document.head.appendChild(script)
        }).catch(error => {
            renderzone.innerHTML = 'Aucun rendu' + modal
            const timeout = setTimeout(() => {
                renderzone.innerHTML = 'Aucun rendu'
            }, 1800)
            document.getElementById("modal").addEventListener("click", () => {
                renderzone.innerHTML = 'Aucun rendu'
                clearTimeout(timeout)
            })
            console.log("Error while fetching animation: " + error)
        })
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
renderzone.innerHTML = 'Aucun rendu'