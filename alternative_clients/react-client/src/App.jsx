import useRobiClient from './hooks/useRobiClient';
import CodingView from './components/CodingView';
import Debug from './components/Debug';
import Loader from './components/Loader';
import Renderer from './components/Renderer';
import ErrorModal from './components/ErrorModal';
import { useState } from 'react';

function App() {

  const [debug, setDebug] = useState(false)

  const {
    current,
    loading,
    direct,
    isLast,
    error,
    info,
    fetch,
    next,
    toggleError,
    toggleDirect
  } = useRobiClient()

  return <div className="container mt-5">
    <CodingView submitCallback={fetch} direct={direct} toggleDirect={toggleDirect} next={next} isLast={isLast} />
    <hr />
    <div className="container">
      <h1>Debugger</h1>
      <label className='form-check-label' htmlFor='showDebug'>Afficher les informations de debug: </label>
      <input id="showDebug" type="checkbox" className="form-check-input mx-1 mb-3" onChange={() => setDebug(!debug)} checked={debug} />
      {debug && <Debug info={info} />}
    </div>
    <div className="mb-3">
      <hr />
      <h1>Espace de rendu</h1>
      {(!loading && <Renderer current={current} />) || <Loader />}
    </div>
    {error && <ErrorModal callback={toggleError} />}
  </div>
}

export default App
