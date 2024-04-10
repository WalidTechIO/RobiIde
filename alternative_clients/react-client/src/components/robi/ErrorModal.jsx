import { useEffect } from "react";

export default function ErrorModal({callback}) {

    useEffect(() => {
        const timeout = setTimeout(callback, 1800)

        return () => {
            clearTimeout(timeout)
        }
    }, [])

    const style = {
        position: "absolute",
        bottom: "0",
        right: "20px"
    }

    return <div style={style}>
        <div className="alert alert-danger alert-dismissible fade show " role="alert">
            Error while fetching data
            <button type="button" className="btn-close" onClick={callback}></button>
        </div>
    </div>

}