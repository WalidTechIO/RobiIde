import { useReducer, useEffect } from "react"
import Renderer from "../components/robi/Renderer.jsx"
import Loader from "../components/robi/Loader.jsx"
import Debug from "../components/robi/Debug.jsx"
import CodingView from "../components/robi/CodingView.jsx";
import ErrorModal from "../components/robi/ErrorModal.jsx";
import { useState } from "react";

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

    const [error, setError] = useState("")
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
        .catch(() => setError("Error while fetching data"))
        .finally(() => dispatch({type: "SET_LOADING", loading: false}))
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

    const direct = state.direct

    const isLast = state.instPtr === state.data.length

    const errorModalCallback = () => {
        setError("")
    }

    console.log("renderer robiclient")

    const errorModal = (error === "") ? <></> : <ErrorModal callback={errorModalCallback} error={error}/>

    const renderer = <div className="mb-3"><hr /><h1>Espace de rendu</h1>{(!state.loading && <Renderer state={state} />) || <Loader />}</div>

    const debug = <Debug info={state.info} />

    const codingview = <CodingView submitCallback={fetchData} direct={direct} setDirect={setDirect} setFiles={setFiles} next={next} isLast={isLast} />

    const robiclient = <>{codingview}{debug}{renderer}{errorModal}</>

    return robiclient
}