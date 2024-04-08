import React, {useState} from 'react';

export default function Debug({info}) {

    const [selectedRef, setSelectedRef] = useState(Object.keys(info.env).length > 0 ? Object.keys(info.env)[0] : "")
    const [showDebug, setShowDebug] = useState(false)

    if (info.stack.length === 0 || Object.keys(info.env).length === 0) {
        return <><hr /><div className="container">
            <h1>Debugger</h1>
            <p>Aucune information n'est présente</p>
        </div></>
    }

    const selectChildrens = [];

    Object.keys(info.env).forEach((key) => {
        selectChildrens[selectChildrens.length] = <option key={key} value={key}>{key}</option>
    })

    return <><hr/><div className="container">
        <h1>Debugger</h1>
        <label className='form-check-label' htmlFor='showDebug'>Afficher les informations de debug: </label>
        <input id="showDebug" type="checkbox" className="form-check-input mx-1 mb-3" onChange={(e) => setShowDebug(!showDebug)} checked={showDebug} />
        {showDebug && <>
            <br/>
            <label className='form-label' htmlFor='refSelector'>References :</label>
            <select id="refSelector" className="form-control mb-3" onChange={(e) => setSelectedRef(e.target.value)} >
                {selectChildrens.map(children => children)}
            </select>
            <div className='h-stack mb-3'>
                <div>
                    <label htmlFor='refInfo' className='form-label'>Information sur {selectedRef === "" ? "..." : selectedRef}</label>
                    <textarea id="refInfo" className="form-control" disabled={true} value={ selectedRef === "" ? "..." : (

                        `Type: ${info.env[selectedRef].className}
Primitives:\n\t- ${info.env[selectedRef].primitives.join("\n\t- ")}
Scripts:\n`
+ Object.keys(info.env[selectedRef].scripts).map(scriptName => {
    return `       -${scriptName}:
            Nb params: ${info.env[selectedRef].scripts[scriptName].nbParams}
            Prototpye: ${info.env[selectedRef].scripts[scriptName].proto}
            Expression: ${info.env[selectedRef].scripts[scriptName].expr}`
}).join("\n"))

                    }/>
                </div>
                <div>
                    <label htmlFor='refInfo' className='form-label'>Liste des appels</label>
                    <textarea className="form-control" disabled={true} value={info.stack.join("\n")} />
                </div>
            </div>
        </>}
    </div></>
}