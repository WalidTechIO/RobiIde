import { useReducer, useEffect } from "react"
import Renderer from "../components/Renderer.jsx"
import Loader from "../components/Loader.jsx"
import Debug from "../components/Debug.jsx"

function reducer(state, action) {
    switch (action.type) {
        case 'SET_LOADING': {
            return {
                ...state,
                loading: action.loading
            }
        }

        case 'SET_DATA': {
            return {
                ...state,
                data: action.data,
                instPtr: 0,
                current: {},
                info: {
                    stack: [],
                    env: {}
                }
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
                    info: {
                        stack: [...state.info.stack, state.data[state.instPtr].resp.info.expr],
                        env: state.data[state.instPtr].resp.info.env
                    }
                }
            }
            return {
                ...state,
                current: {},
                instPtr: 0,
                info: {
                    stack: [],
                    env: {}
                }
            }
        }
    }
}

export default function useRobiClient(initial = {
    instPtr: 0,
    loading: false,
    direct: true,
    data: {},
    current: {},
    files: [],
    info: {
        stack: [],
        env: {}
    }
}) {
    //TODO: Mettre l'etat dans un contexte
    const [state, dispatch] = useReducer(reducer, initial)

    useEffect(() => {
        if(state.direct && state.data.length > state.instPtr) {
            const delay = state.data[state.instPtr].delay - (state.data[state.instPtr - 1] ? state.data[state.instPtr - 1].delay : 0)
            setTimeout(next, delay)
        }
    }, [state.current])

    const fetchData = (ip, port, program) => {
        dispatch({ type: "SET_LOADING", loading: true })
        fetch(`http://${ip}:${port}/world`, {
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
        .catch(() => alert("Error while fetching data"))
        .finally(() => dispatch({type: "SET_LOADING", loading: false}))
    }

    const renderer = <div className="mb-3"><hr/><h1>Espace de rendu</h1>{(!state.loading && <Renderer state={state} />) || <Loader />}</div>

    const reset = (state.direct && state.data.length == state.instPtr && state.data.length != 0) ? <button type="button" className="mx-1 btn btn-dark" onClick={() => next()}>Reset</button> : <></>

    const debug = <Debug info={state.info} />

    const setDirect = (value) =>  {
        dispatch({type: "SET_DIRECT", value: value})
    }

    const setFiles = (files) => {
        dispatch({type: "SET_FILES", files: files})
    }

    const next = () => {
        dispatch({ type: "NEXT"})
    }

    return {
        state,
        fetchData,
        renderer,
        reset,
        debug,
        setDirect,
        setFiles,
        next,
    }
}