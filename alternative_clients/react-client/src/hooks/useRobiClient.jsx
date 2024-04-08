import { useReducer, useEffect } from "react"
import Renderer from "../components/Renderer.jsx"
import Loader from "../components/Loader.jsx"

function reducer(state, action) {
    switch (action.type) {
        case 'SET_LOADING': {
            return {
                ...state,
                loading: action.loading
            }
        }

        case 'SET_PROGRAM': {
            return {
                ...state,
                program: action.program
            }
        }

        case 'SET_IP': {
            return {
                ...state,
                ip: action.ip
            }
        }

        case 'SET_PORT': {
            return {
                ...state,
                port: action.port
            }
        }

        case 'SET_DATA': {
            return {
                ...state,
                data: action.data,
                instPtr: 0,
                current: {}
            }
        }

        case 'SET_DIRECT': {
            return {
                ...state,
                direct: action.value
            }
        }

        case 'SET_FILES': {
            return {
                ...state,
                files: action.files
            }
        }

        case 'NEXT': {
            if(state.instPtr < state.data.length) {
                return {
                    ...state,
                    current: state.data[state.instPtr],
                    instPtr: state.instPtr + 1,
                }
            }
            return {
                ...state,
                current: {},
                instPtr: 0
            }
        }
    }
}

export default function useRobiClient(initial = {
    instPtr: 0,
    loading: false,
    ip: "",
    port: "",
    direct: true,
    data: {},
    current: {},
    files: []
}) {
    const [state, dispatch] = useReducer(reducer, initial)

    useEffect(() => {
        if(state.direct && state.data.length > state.instPtr) {
            const delay = state.data[state.instPtr].delay - (state.data[state.instPtr - 1] ? state.data[state.instPtr - 1].delay : 0)
            setTimeout(next, delay)
        }
    }, [state.current])

    const fetchData = (program) => {
        dispatch({ type: "SET_LOADING", loading: true })
        fetch(`http://${state.ip}:${state.port}/world`, {
            method: "POST", 
            body: JSON.stringify({
                type: "PROG",
                program: {
                    mode: "DIRECT",
                    contenu: program
                }
            }
        )})
        .then(r => r.json())
        .then(json => dispatch({type: "SET_DATA", data: json}))
        .catch(() => console.log("Error while fetching data"))
        .finally(() => dispatch({type: "SET_LOADING", loading: false}))
    }

    const reset = (state.direct && state.data.length == state.instPtr && state.data.length != 0) ? <button type="button" className="mx-1 btn btn-dark" onClick={() => next()}>Reset</button> : <></>

    const setProgram = (program) => {
        dispatch({type: "SET_PROGRAM", program: program})
    }

    const setIp = (ip) => {
        dispatch({ type: "SET_IP", ip: ip })
    }

    const setPort = (port) => {
        dispatch({ type: "SET_PORT", port: port })
    }

    const setDirect = (value) =>  {
        dispatch({type: "SET_DIRECT", value: value})
    }

    const setFiles = (files) => {
        dispatch({type: "SET_FILES", files: files})
    }

    const next = () => {
        dispatch({ type: "NEXT"})
    }

    const renderer = <><h1>Espace de rendu</h1>{(!state.loading && <Renderer state={state}/>) || <Loader />}</>

    return {
        state,
        setPort,
        setIp,
        setProgram,
        setDirect,
        setFiles,
        fetchData,
        renderer,
        next,
        reset
    }
}