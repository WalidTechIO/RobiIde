import { useReducer, useEffect, useMemo } from "react"

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
                current: -1,
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
            if (state.current + 1 < state.data.length) {
                return {
                    ...state,
                    current: state.current + 1,
                    info: {
                        stack: [...state.info.stack, state.data[state.current+1].resp.info.expr],
                        env: state.data[state.current+1].resp.info.env
                    }
                }
            }
            return {
                ...state,
                current: -1,
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
    }
}

export default function useRobiClient() {

    const [state, dispatch] = useReducer(reducer, {
        current: -1,
        loading: false,
        direct: true,
        error: false,
        data: {},
        files: [],
        info: {
            stack: [],
            env: {}
        },
    })

    const {fetchData, setFiles, next, errorModalCallback, toggleDirect} = useMemo(() => {
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

        const setFiles = (files) => {
            dispatch({ type: "SET_FILES", files: files })
        }

        const next = () => {
            dispatch({ type: "NEXT" })
        }

        const errorModalCallback = () => {
            dispatch({type: "TOGGLE_ERROR"})
        }

        return {fetchData, setFiles, next, errorModalCallback, toggleDirect}
    }, [])

    useEffect(() => {
        if(state.direct && state.data.length > state.current + 1) {
            const delay = state.data[state.current + 1].delay - (state.data[state.current]?.delay || 0)
            setTimeout(next, delay)
        }
    })

    return { 
        fetch: fetchData,
        setFiles,
        next,
        errorModalCallback,
        toggleDirect,
        files: state.files,
        direct: state.direct,
        info: state.info,
        loading: state.loading,
        isLast: state.current + 1 === state.data.length,
        error: state.error,
        current: state.data[state.current]?.resp
    }
}