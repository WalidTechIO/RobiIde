import { useReducer, useEffect } from "react"
import Renderer from "../components/robi/Renderer.jsx"
import Loader from "../components/robi/Loader.jsx"
import Debug from "../components/robi/Debug.jsx"
import CodingView from "../components/robi/CodingView.jsx";
import ErrorModal from "../components/robi/ErrorModal.jsx";
import { useMemo } from "react";

function reducer(state, action) {
    switch (action.type) {
        case 'TOGGLE_LOADING': {
            return {
                ...state,
                loading: !state.loading
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

        case 'TOGGLE_DIRECT': {
            return {
                ...state,
                direct: !state.direct
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

        case 'TOGGLE_ERROR': {
            return {
                ...state,
                error: !state.error
            }
        }

        case 'TOGGLE_DEBUG': {
            return {
                ...state,
                showDebug: !state.showDebug
            }
        }
    }
}

export default function useRobiClient(initial = {
    instPtr: 0,
    loading: false,
    direct: true,
    showDebug: false,
    error: false,
    data: {},
    current: {},
    files: [],
    info: {
        stack: [],
        env: {}
    },
}) {

    const [state, dispatch] = useReducer(reducer, initial)

    const {fetchData, setFiles, next, errorModalCallback, toggleDirect, toggleDebug} = useMemo(() => {
        const fetchData = (ip, port, program) => {
            dispatch({ type: "TOGGLE_LOADING"})
            fetch(`http://${ip}:${port}/world`, {
                method: "POST",
                body: JSON.stringify({
                    type: "PROG",
                    program: {
                        mode: "DIRECT",
                        contenu: program
                    }
                }
                )
            })
                .then(r => r.json())
                .then(json => dispatch({ type: "SET_DATA", data: json }))
                .catch(() => dispatch({type: "TOGGLE_ERROR"}))
                .finally(() => dispatch({ type: "TOGGLE_LOADING"}))
        }

        const toggleDirect = () => {
            dispatch({ type: "TOGGLE_DIRECT"})
        }

        const toggleDebug = () => {
            dispatch({type: "TOGGLE_DEBUG"})
        }

        const setFiles = (files) => {
            dispatch({ type: "SET_FILES", files: files })
        }

        const next = () => {
            dispatch({ type: "NEXT" })
        }

        const errorModalCallback = () => {
            dispatch({type: "TOGGLE_ERROR"})
        }

        return {fetchData, setFiles, next, errorModalCallback, toggleDirect, toggleDebug}
    }, [])

    useEffect(() => {
        if(state.direct && state.data.length > state.instPtr) {
            const delay = state.data[state.instPtr].delay - (state.data[state.instPtr - 1] ? state.data[state.instPtr - 1].delay : 0)
            setTimeout(next, delay)
        }
    }, [state.current])

    const errorModal = !state.error ? <></> : <ErrorModal callback={errorModalCallback} />

    const renderer = <div className="mb-3"><hr /><h1>Espace de rendu</h1>{(!state.loading && <Renderer state={state} />) || <Loader />}</div>

    const debug = <Debug info={state.info} />

    const codingview = <CodingView submitCallback={fetchData} direct={state.direct} toggleDirect={toggleDirect} setFiles={setFiles} next={next} isLast={state.instPtr === state.data.length} />

    const robiclient = <>
        {codingview}
        <hr />
        <div className="container">
            <h1>Debugger</h1>
            <label className='form-check-label' htmlFor='showDebug'>Afficher les informations de debug: </label>
            <input id="showDebug" type="checkbox" className="form-check-input mx-1 mb-3" onChange={toggleDebug} checked={state.showDebug} />
            {state.showDebug && <Debug info={state.info} />}
        </div>
        {renderer}
        {errorModal}
    </>

    return robiclient
}