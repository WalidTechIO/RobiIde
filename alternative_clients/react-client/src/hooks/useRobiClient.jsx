import { useReducer } from "react"
import Renderer from "../components/Renderer.jsx"
import { useEffect } from "react"

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

        case 'NEXT': {
            if(state.instPtr < state.data.length) {
                const delay = state.data[state.instPtr].delay - (state.data[state.instPtr - 1] ? state.data[state.instPtr - 1].delay : 0)
                const date = Date.now()
                let currentDate = null
                do {
                    currentDate = Date.now()
                    if(currentDate - date >= delay) {
                        return {
                            ...state,
                            current: state.data[state.instPtr],
                            instPtr: state.instPtr + 1,
                        }
                    }
                } while(currentDate - date < delay)
            }
            return state
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
    current: {}
}) {
    const [state, dispatch] = useReducer(reducer, initial)

    useEffect(() => {
        if(state.data.length > state.instPtr && state.direct) {
            next()
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
        .finally(() => dispatch({type: "SET_LOADING", loading: false}))
    }

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

    const next = () => {
        dispatch({ type: "NEXT"})
    }

    const renderer = <Renderer state={state}/>

    return {
        state,
        setPort,
        setIp,
        setProgram,
        setDirect,
        fetchData,
        renderer,
        next
    }
}