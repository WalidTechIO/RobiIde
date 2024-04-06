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

const handleSubmit = (e) => {
    e.preventDefault();
    const data = new FormData(e.target);
    fetchScript(data.get("ip"), data.get("port"), {
        type: "PROG",
        program: {
            mode: "DIRECT",
            contenu: data.get("program")
        }
    });
}

document.getElementById("form").addEventListener("submit", handleSubmit)