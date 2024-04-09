import React, {useState} from 'react';

export default function Debug({info}) {

    const [selectedRef, setSelectedRef] = useState(Object.keys(info.env).length > 0 ? Object.keys(info.env)[0] : "")
    const [showDebug, setShowDebug] = useState(false)

    const selectChildrens = [];

    const finalRef = (!info.env[selectedRef]) ? (Object.keys(info.env).length > 0 ? Object.keys(info.env)[0] : "") : selectedRef

    const finalRefScriptInfo = (info.env[finalRef] && Object.keys(info.env[finalRef].scripts).length > 0) ?
    Object.keys(info.env[finalRef].scripts).map(scriptName => {
        return `\t\t-${scriptName}:\n\t\t\tNb params: ${info.env[finalRef].scripts[scriptName].nbParams}\n\t\t\tPrototpye: ${info.env[finalRef].scripts[scriptName].proto}\n\t\t\tExpression: ${info.env[finalRef].scripts[scriptName].expr.replaceAll("\n", "\n\t\t\t\t")}`
    }).join("\n").replace(/^/, "\n\tScripts:\n") : ""

    const refInfo = info.env[finalRef] ? `Type: ${info.env[finalRef].className}\n\tPrimitives:\n\t\t- ${info.env[finalRef].primitives.join("\n\t\t- ")}${finalRefScriptInfo}` : ""

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
            <select id="refSelector" className="form-control mb-3" onChange={(e) => setSelectedRef(e.target.value)} value={finalRef} disabled={selectChildrens.length === 0}>
                {selectChildrens.map(children => children)}
            </select>
            <div className='mb-3'>
                <div>
                    <label htmlFor='refInfo' className='form-label'>{finalRef === "" ? "Aucune référence séléctionnée" : "Information sur " + finalRef}</label>
                    <textarea id="refInfo" className="form-control" disabled={true} value={refInfo}/>
                </div>
                <div>
                    <label htmlFor='refInfo' className='form-label'>Liste des appels</label>
                    <textarea className="form-control" disabled={true} value={info.stack.join("\n")} />
                </div>
            </div>
        </>}
    </div></>
}