import React, {useState} from 'react';

export default function Environment({env}) {

    const [selectedRef, setSelectedRef] = useState("")
    const [showEnv, setShowEnv] = useState(false)

    const selectChildrens = [];

    Object.keys(env.env).forEach((key) => {
        selectChildrens[selectChildrens.length] = <option key={key} value={key}>{key}</option>
    })

    return <>
        <input type="check" className="btn btn-dark my-2" onChange={(e) => setShowEnv(!showEnv)} checked={showEnv}>Afficher l'environment</input>
        <select onChange={(e) => setSelectedRef(e.target.value)} >
            {selectChildrens.map(children => children)}
        </select>
    </>
}