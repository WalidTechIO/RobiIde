import { useEffect } from "react";

export default function ErrorModal({error, callback}) {

    useEffect(() => {
        setTimeout(callback, 3000)
    })

    const style = {
        position: "absolute",
        bottom: "0",
        right: "20px"
    }

    return <div style={style}>
        <div className="alert alert-danger alert-dismissible fade show " role="alert">
            {error}
            <button type="button" className="btn-close" onClick={callback}></button>
        </div>
    </div>

}