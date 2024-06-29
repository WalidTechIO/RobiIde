import { useReducer, useEffect, useMemo } from "react"

function reducer(state, action) {

    switch (action.type) {
        
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

        case 'NEXT': {
            if (state.current + 1 < state.data.length) {
                return {
                    ...state,
                    current: state.current + 1,
                    info: {
                        stack: [...state.info.stack, state.data[state.current + 1].resp.info.expr],
                        env: state.data[state.current + 1].resp.info.env
                    }
                }
            }
            return {
                ...state,
                current: state.data[0] ? 0 : -1,
                info: {
                    stack: [state.data[0]?.resp.info.expr],
                    env: state.data[0]?.resp.info.env || {}
                }
            }
        }
        
        case 'TOGGLE_LOADING': {
            return {
                ...state,
                loading: !state.loading
            }
        }

        case 'TOGGLE_DIRECT': {
            return {
                ...state,
                direct: !state.direct
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

    const [{
        current,
        loading,
        direct,
        error,
        data,
        info
    }, dispatch] = useReducer(reducer, {
        current: -1,
        loading: false,
        error: false,
        direct: true,
        data: [],
        info: {
            stack: [],
            env: {}
        },
    })

    const { fetchData, next, toggleError, toggleDirect } = useMemo(() => {
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

        const next = () => {
            dispatch({ type: "NEXT" })
        }

        const toggleDirect = () => {
            dispatch({ type: "TOGGLE_DIRECT"})
        }

        const toggleError = () => {
            dispatch({type: "TOGGLE_ERROR"})
        }

        return { fetchData, next, toggleError, toggleDirect }
    }, [])

    useEffect(() => {
        if(direct && data.length > current + 1) {
            const delay = data[current + 1].delay - (data[current]?.delay || 0)
            setTimeout(next, delay)
        }
    })

    return { 
        fetch: fetchData,
        next,
        toggleError,
        toggleDirect,
        direct,
        info,
        loading,
        error,
        isLast: current + 1 === data.length,
        current: data[current]?.resp
    }
}